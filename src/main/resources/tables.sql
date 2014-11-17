drop TABLE assoc;
drop table entity;
drop table chronicle;

CREATE TABLE entity (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT entity_pk PRIMARY KEY,
    noun VARCHAR(100) DEFAULT NULL,
    description VARCHAR(100) DEFAULT NULL,
    inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- CREATE TABLE tag (
--     id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT tag_pk PRIMARY KEY,
--     tag VARCHAR(100) DEFAULT NULL,
--     inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );
-- insert into tag (tag) values ('przerwa');

CREATE TABLE chronicle (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT chron_pk PRIMARY KEY,
    tag VARCHAR(100) DEFAULT NULL,
    description VARCHAR(100) DEFAULT NULL,
    inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP
--     CONSTRAINT tag_fk FOREIGN KEY (tag) REFERENCES tag (id),
);


CREATE TABLE assoc (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT eta_pk PRIMARY KEY,
    entity1Id INT NOT NULL,
    entity2Id INT DEFAULT 0,
    noun VARCHAR(100) DEFAULT NULL,
    description VARCHAR(100) DEFAULT NULL,
    inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT entity_fk FOREIGN KEY (entity1Id) REFERENCES entity (id),
    CONSTRAINT propCh CHECK (entity2Id != entity1Id)
);

insert into entity (noun, description) values ('Randka', 'Widziałem się z Jolą, miała smutną twarz.');
insert into entity (noun, description) values ('Restauracja', 'Restauracja Damiana');
insert into entity (noun, description) values ('Miasto', 'Łódź');
insert into entity (noun, description) values ('Ulica', '6 Sierpnia');
insert into entity (noun, description) values ('Projekt', 'Robię stronę Łukasza');
insert into entity (noun, description) values ('Story', 'Wdrażam logowanie');
insert into entity (noun, description) values ('Task', 'Szukam danych na necia o logowaniu.');
insert into entity (noun, description) values ('Break', 'Przerwa na kawę w kantynie.');
insert into entity (noun, description) values ('Work', '');
insert into assoc (entity1Id, entity2Id, noun, description) values (1, 2, 'social', 'randka - restauracje, ');
insert into assoc (entity1Id, entity2Id, noun) values (2, 3, 'address');
insert into assoc (entity1Id, entity2Id, noun) values (2, 4, 'address');
insert into assoc (entity1Id, entity2Id) values (5, 6);
insert into assoc (entity1Id, entity2Id) values (6, 7);
insert into assoc (entity1Id, entity2Id) values (7, 8);
insert into assoc (entity1Id, entity2Id) values (7, 9);

-- current is the last one
-- entity has properties
-- if property has property it becomes entity
-- entities are whatever has entity1Id in assoc

-- select * from TEKA.ASSOC;
-- select ass.entity2Id from ENTITY en join ASSOC ass on en.ID=ass.ENTITY1ID where ass.ENTITY1ID=2;
-- select * from ENTITY en where en.ID in 
-- (select ass.entity2Id from ENTITY en join ASSOC ass on en.ID=ass.ENTITY1ID where ass.ENTITY1ID=2);