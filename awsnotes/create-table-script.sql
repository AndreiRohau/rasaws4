/*
mysql -u admin -p -h m7rdsar.cy36de7il6no.us-east-1.rds.amazonaws.com -P 3306
*/
USE imagesdb;

INSERT INTO images (full_name, name, extension, file_size, last_modified_date) VALUES ("PICTURE-Test.JPG", "PICTURE", "JPG", 4444, "2021-05-20T00:00:00");

CREATE TABLE IF NOT EXISTS images (
    fullName VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    extension VARCHAR(255) NOT NULL,
	fileSize BIGINT,
	lastModifiedDate TIMESTAMP NOT NULL,
	PRIMARY KEY (fullName)
);
