-- ADMIN AND MALL_OWNER
INSERT INTO users
VALUES ('1910d486-60de-4955-a953-c11a042eb025',
        '$2a$12$4tj3NyfOo16O2Y27UyTw3uI.m6U5Zptl/aoLu7mMF9aBJ5teWAYe.',
        'Admin Adminov',
        'admin@admin.com',
        '0000000000',
        'ADMIN',
        NULL
);

INSERT INTO users
VALUES ('a391e987-fd06-4426-ba02-1d37daeac9e4',
        '$2a$12$ih/hdBq.H6bfnrhZsaQxgOvgF3kQbdHmDOxyUT2x051VLtYxBsh4K',
        'Ivan Dimitrov',
        'mall@owner.com',
        '0877452166',
        'MALL_OWNER',
        NULL
);

-- MALL
INSERT INTO malls
VALUES (
        'a0ed2e96-a4a5-4027-a732-96d46875552c',
        'Mall Veliko Tarnovo',
        4,
        'Oborishte 18',
        'The mall in Veliko Tarnovo',
        'a391e987-fd06-4426-ba02-1d37daeac9e4'
);

-- STORE MANAGER AND STORE
INSERT INTO users
VALUES ('022c819b-bcb5-4ee2-b2d2-20464f357239',
        '$2a$12$5QmDAVwW/BZRi8kzjn0ewOocdCCi5OEVbJrrcQyGWA8udxy1j8miW',
        'Georgi Petrov',
        'gosho@g.com',
        '0882341957',
        'MANAGER',
        NULL
);

INSERT INTO stores
VALUES ('22f79f12-82d1-4609-a4e3-c4b32fc12663',
        'H&M',
        2,
        '022c819b-bcb5-4ee2-b2d2-20464f357239',
        'a0ed2e96-a4a5-4027-a732-96d46875552c'
);

-- Set store_id for the manager
UPDATE users
SET store_id = '22f79f12-82d1-4609-a4e3-c4b32fc12663'
WHERE id = '022c819b-bcb5-4ee2-b2d2-20464f357239';

-- EMPLOYEES for the Store
INSERT INTO users
VALUES ('5035d870-58d6-4df0-93d4-0a4d61239291',
        '$2a$12$NqJEfhEWLxQq6CwzjyGff.zUJO7DkjDBJzJH3jBh0ToHOiihQEe3W',
        'Denislav Iliev',
        'deni@iliev.com',
        '0877412985',
        'EMPLOYEE',
        '22f79f12-82d1-4609-a4e3-c4b32fc12663'
);

INSERT INTO users
VALUES ('b8b56d59-9829-456b-8bb1-afbdd39e6a42',
        '$2a$12$.X2JmsVzX7ucAB.BcLJf1evDMcxhSs.8kMgfEW/RFSY1DF5vXN6jS',
        'Ivana Georgieva',
        'ivana@georgieva.com',
        '0899351467',
        'EMPLOYEE',
        '22f79f12-82d1-4609-a4e3-c4b32fc12663'
);