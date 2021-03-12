update dict.pix_key set situation = 'ACTIVE' where situation = '' and reason != 'INACTIVITY';
update dict.pix_key set situation = 'INACTIVE' where situation = '' and reason = 'INACTIVITY';