CREATE TABLE `orders` (
`id` INT AUTO_INCREMENT PRIMARY KEY,
`product_name` VARCHAR(255) NOT NULL,
`quantity` INT NOT NULL,
`expiration_date` DATE NOT NULL
);