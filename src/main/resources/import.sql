do $$
    declare
        address_id1 address.id%TYPE;
        address_id2 address.id%TYPE;
        address_id3 address.id%TYPE;
    begin

        select nextval('hibernate_sequence') into address_id1;
        insert into address (id, city, complementary_address, main_address, zip_code) values (address_id1,'Gif-sur-Yvette','CentraleSupélec
3 rue Joliot Curie','',91190);

        select nextval('hibernate_sequence') into address_id2;
        insert into address (id, city, complementary_address, main_address, zip_code) values (address_id2,'avenue du président Wilson
94235 Cachan','Ecole Normale Supérieure Paris-Saclay','',94235);

        select nextval('hibernate_sequence') into address_id3;
        insert into address (id, city, complementary_address, main_address, zip_code) values (address_id3,'Gif-sur-Yvette','CESAL 1 rue Joliot Curie','',91190);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','','1001 Vies Habitat',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','5590Z','ASL 26',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','','Campus Agro SAS',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','8542Z','CentraleSupélec',address_id1,false);
--         insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','8542Z','CentraleSupélec',address_id1,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','9499Z','CESAL',address_id1,false);
        --insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','9499Z','CESAL',address_id2,false);
        --insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','9499Z','CESAL',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','','Commune de Gif-sur-Yvette',address_id1,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','5590Z','CROUS',address_id1,false);
--         insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','5590Z','CROUS',address_id1,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','8542Z','Ecole Normale Supérieure Paris-Saclay',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','562920','Ecole Normale Supérieure Paris-Saclay',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','8542Z','ENSAE',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','6820B','Foncia GIV',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','5510Z','Innocity/Campanile',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','562920','Institut Mines Télécom',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','8542Z','Institut Mines-Télécom',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','','Locafimo',address_id1,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','','Platon-Saclay',address_id1,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','','Région Ile de France',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','R','SCCV Gif Moulon A2',address_id1,false);
--         insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','R','SCCV Gif Moulon A2',address_id1,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','R','SCCV Gif Moulon A3',address_id1,false);
--         insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','R','SCCV Gif Moulon A3',address_id1,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','R','SCCV Gif Moulon A4',address_id1,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','R','SCCV Gif Moulon B4B5',address_id1,false);
--         insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','R','SCCV Gif Moulon B4B5',address_id1,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','','Servier',address_id1,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','','Stop&Work Paris-Saclay',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','8542Z','Université Paris Sud',address_id2,false);
        insert into subscriber(id, active, created_by, codenaf, subscriber_name, address_id,has_account) values (nextval('hibernate_sequence'),true,'super_admin@yopmail.com','8542Z','Université Paris-Saclay',address_id2,false);
    END $$ ;
