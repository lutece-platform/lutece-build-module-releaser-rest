
--
-- Structure for table releaser_rest_site
--

DROP TABLE IF EXISTS releaser_rest_site;
CREATE TABLE releaser_rest_site (
id_site int AUTO_INCREMENT,
id_site int default '0' NOT NULL,
artifact_id varchar(50) default '',
scm_url varchar(255) default '',
name varchar(255) default '',
description varchar(255) default '',
PRIMARY KEY (id_site)
);
