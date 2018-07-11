export MASTER_IP=172.16.40.130
export KUBECONFIG=/var/lib/kubelet/kubelet-config
export PROBLEM_REPORT=$MASTER_IP-problem-report.txt
echo "==========================" >> $PROBLEM_REPORT
echo "      SYSTEM INFO" >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
uname -a >>  $PROBLEM_REPORT
cat /proc/meminfo >>  $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
echo "      DOCKER IMAGES" >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
docker images >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
echo "     KUBELET STATUS:" >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
systemctl status kubelet >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
echo "     DOCKER STATUS:" >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
systemctl status docker >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
echo "     MASTER NODE INFO" >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
kubectl -n kube-system describe node $MASTER_IP >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
echo "     POD INFO" >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
kubectl get pods --all-namespaces \
  -o=custom-columns=Namespace:.metadata.namespace,Name:.metadata.name,Status:.status.phase,Message:'{.status.conditions[*].message}' >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
echo "     KUBELET LOGS " >>  $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
journalctl -u kubelet -o cat >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
echo "     DOCKER LOGS" >> $PROBLEM_REPORT
echo "==========================" >> $PROBLEM_REPORT
journalctl -u docker -o cat >> $PROBLEM_REPORT
kubectl get pods --all-namespaces -o=custom-columns=Namespace:.metadata.namespace,Name:.metadata.name,Status:.status.phase,Message:'{.status.conditions[*].message}' \
--no-headers=True | grep -vE 'Running|Succeeded' | \
awk '{split($0,a," "); print "kubectl -n " a[1] " describe pod " a[2];}' | \
bash > $PROBLEM_REPORT-pod-details.txt
echo "ACTION REQUIRED: Logs collected to $PROBLEM_REPORT and $PROBLEM_REPORT-pod-details.txt. Please attach both files to your issue."
