INSERT INTO USERS (id, login, registration_date, email, password)
VALUES (1001, 'basia123', CURRENT_TIMESTAMP, 'basia@gmail.com', 'kochamMatche4'),
       (1002, 'franek456', CURRENT_TIMESTAMP, 'franek@gmail.com', 'nienawidzeMas5');


INSERT INTO server (id, owner_id, name) VALUES (2001,1001, 'my-server-name');