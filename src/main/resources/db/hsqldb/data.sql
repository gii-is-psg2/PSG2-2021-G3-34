-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- owner users
INSERT INTO users(username,password,enabled) VALUES ('george','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'george','owner');
INSERT INTO users(username,password,enabled) VALUES ('betty','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (20,'betty','owner');
INSERT INTO users(username,password,enabled) VALUES ('eduardo','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (21,'eduardo','owner');
INSERT INTO users(username,password,enabled) VALUES ('harold','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (22,'harold','owner');
INSERT INTO users(username,password,enabled) VALUES ('peter','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (23,'peter','owner');
INSERT INTO users(username,password,enabled) VALUES ('jean','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (24,'jean','owner');
INSERT INTO users(username,password,enabled) VALUES ('jeff','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (25,'jeff','owner');
INSERT INTO users(username,password,enabled) VALUES ('maria','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (26,'maria','owner');
INSERT INTO users(username,password,enabled) VALUES ('david','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (27,'david','owner');
INSERT INTO users(username,password,enabled) VALUES ('carlos','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (28,'carlos','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'vet1','veterinarian');

INSERT INTO vets VALUES (1, 'James', 'Carter', 'vet1');
INSERT INTO vets VALUES (2, 'Helen', 'Leary', 'vet1');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas', 'vet1');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega', 'vet1');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens', 'vet1');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins', 'vet1');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'george');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'betty');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'eduardo');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'harold');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'peter');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'jean');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'jeff');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'maria');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'david');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'carlos');




INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (1, 'Leo', '2010-09-07', 1, 1,TRUE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (2, 'Basil', '2012-08-06', 6, 2, TRUE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (3, 'Rosy', '2011-04-17', 2, 3, FALSE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (4, 'Jewel', '2010-03-07', 2, 3, TRUE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (5, 'Iggy', '2010-11-30', 3, 4, TRUE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (6, 'George', '2010-01-20', 4, 5, FALSE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (7, 'Samantha', '2012-09-04', 1, 6, FALSE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (8, 'Max', '2012-09-04', 1, 6, TRUE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (9, 'Lucky', '2011-08-06', 5, 7, FALSE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (10, 'Mulligan', '2007-02-24', 2, 8, FALSE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (11, 'Freddy', '2010-03-09', 5, 9, FALSE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (12, 'Lucky', '2010-06-24', 2, 10, FALSE);
INSERT INTO pets(id,name,birth_date,type_id,owner_id,in_adoption) VALUES (13, 'Sly', '2012-06-08', 1, 10, FALSE);


INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

INSERT INTO bookings VALUES(1, '2021-01-10', 9, '2021-01-03', 3);
INSERT INTO bookings VALUES(2, '2021-01-10', 2, '2021-01-03', 4);
INSERT INTO bookings VALUES(3, '2021-03-20', 11, '2021-03-16', 3);
INSERT INTO bookings VALUES(4, '2021-07-11', 13, '2021-07-10', 1);
INSERT INTO bookings VALUES(5, '2021-04-26', 7, '2021-04-25', 1);
INSERT INTO bookings VALUES(6, '2021-03-15', 11, '2021-03-14', 2);
INSERT INTO bookings VALUES(7, '2021-06-07', 9, '2021-06-06', 2);
INSERT INTO bookings VALUES(8, '2021-08-13', 5, '2021-08-12', 4);
INSERT INTO bookings VALUES(9, '2021-11-14', 4, '2021-11-13', 5);
INSERT INTO bookings VALUES(10, '2021-12-28', 1, '2021-12-27', 5);
INSERT INTO bookings VALUES(11, '2021-10-11', 3, '2021-10-10', 5);


INSERT INTO adoptions(owner,possible_owner,description,adoption_state_type, pet_id) VALUES ('betty','peter','I am a dog',0,2);
INSERT INTO adoptions(owner,possible_owner,description,adoption_state_type, pet_id) VALUES ('eduardo','jean','I am a dog',1,3);
INSERT INTO adoptions(owner,possible_owner,description, adoption_state_type, pet_id) VALUES ('george','jeff','I am a cat',2,1);
