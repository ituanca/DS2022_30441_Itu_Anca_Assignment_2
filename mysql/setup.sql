CREATE USER 'htoh'@'%' IDENTIFIED BY 'htoh' ;

SET PASSWORD FOR 'htoh'@'%' = 'htoh';

ALTER USER 'htoh'@'%' IDENTIFIED WITH mysql_native_password BY 'htoh';

GRANT ALL on *.* TO 'htoh'@'%';

ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'htoh';
