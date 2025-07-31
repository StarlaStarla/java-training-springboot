CREATE TABLE `orders` (
`id` VARCHAR(255) NOT NULL PRIMARY KEY,
`user_id` INT NOT NULL,
`product_name` VARCHAR(255) NOT NULL,
`quantity` INT NOT NULL,
`expiration_date` TIMESTAMP NOT NULL
);