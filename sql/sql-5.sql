select * from login;
select * from profile;
select * from contact;
select * from post;

CREATE TABLE `contact` (
  `email` varchar(255) NOT NULL,
  `owner_id` bigint NOT NULL,
  `image` longtext,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`email`,`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
