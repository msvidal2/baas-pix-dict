alter table claim add column pix_key_fk varchar(100) NULL;
alter table claim add column key_type_fk varchar(10) NULL;
alter table claim add FOREIGN KEY (pix_key_fk, key_type_fk) REFERENCES pix_key(pix_key, type);