# I know we always need to start by hello!
def hello(event, context):
  print(event)
  event['data'] = "Hello " + str(event['data'])
  return event['data']
