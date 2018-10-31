CREATE KEYSPACE IF NOT EXISTS assetmonitoring with replication = {'class':'NetworkTopologyStrategy', 'DC1':1};

CREATE TABLE IF NOT EXISTS assetmonitoring.assets(
    id text PRIMARY KEY, 
    os text, 
    type text, 
    ipaddress text, 
    version text, 
    antivirus text, 
    current double, 
    rotation int, 
    pressure int,
    flowRate bigint 
    temperature int,
    riskRating double, 
    latitude double, 
    longitude double,
    creationDate timestamp
);

CREATE INDEX IF NOT EXISTS ON assetmonitoring.assets (type);

INSERT INTO assetmonitoring.assets (id, os, type, ipaddress, version, antivirus, current, rotation, pressure, flowRate, temperature, riskRating, latitude, longitude, creationDate) values ('1', 'Window', 'Window', '0.0.0.0', '1.0.0', 'Kaspersky', 1, 1, 1, 1, 1, 1, 1, 1, toTimestamp(now()));
INSERT INTO assetmonitoring.assets (id, os, type, ipaddress, version, antivirus, current, rotation, pressure, flowRate, temperature, riskRating, latitude, longitude, creationDate) values ('2', 'Window', 'Window', '0.0.0.0', '2.0.0', 'Kaspersky', 1, 1, 1, 1, 1, 1, 1, 1, toTimestamp(now()));
INSERT INTO assetmonitoring.assets (id, os, type, ipaddress, version, antivirus, current, rotation, pressure, flowRate, temperature, riskRating, latitude, longitude, creationDate) values ('3', 'Window', 'Window', '0.0.0.0', '3.0.0', 'Kaspersky', 1, 1, 1, 1, 1, 1, 1, 1, toTimestamp(now()));
INSERT INTO assetmonitoring.assets (id, os, type, ipaddress, version, antivirus, current, rotation, pressure, flowRate, temperature, riskRating, latitude, longitude, creationDate) values ('4', 'Window', 'Window', '0.0.0.0', '4.0.0', 'Kaspersky', 1, 1, 1, 1, 1, 1, 1, 1, toTimestamp(now()));

CREATE TABLE IF NOT EXISTS assetmonitoring.event_metrics(
    id text PRIMARY KEY,
    current double,
    rotation int,
    pressure int,
    temperature int,
    timecreated timestamp
);

CREATE INDEX IF NOT EXISTS ON assetmonitoring.event_metrics(timecreated);

INSERT INTO assetmonitoring.event_metrics (id, current, rotation, pressure, temperature, timecreated) values ('1', 1, 1, 1, 1, '2018-10-01T21:01:54.001+0000');
INSERT INTO assetmonitoring.event_metrics (id, current, rotation, pressure, temperature, timecreated) values ('3', 3, 3, 3, 3, toTimestamp(now()));
INSERT INTO assetmonitoring.event_metrics (id, current, rotation, pressure, temperature, timecreated) values ('4', 4, 4, 4, 4, toTimestamp(now()));