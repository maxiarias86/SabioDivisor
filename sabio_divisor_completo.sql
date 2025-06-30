-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 01-07-2025 a las 01:39:48
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

DROP DATABASE IF EXISTS sabio_divisor;
CREATE DATABASE sabio_divisor;
USE sabio_divisor;

-- --------------------------------------------------------

-- Estructura de tabla para la tabla `debts`
CREATE TABLE `debts` (
  `id` int(11) NOT NULL,
  `amount` double NOT NULL CHECK (`amount` > 0),
  `creditor_id` int(11) NOT NULL,
  `debtor_id` int(11) NOT NULL,
  `expense_id` int(11) NOT NULL,
  `due_date` date NOT NULL,
  `installment_number` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcado de datos para la tabla `debts`
INSERT INTO `debts` (`id`, `amount`, `creditor_id`, `debtor_id`, `expense_id`, `due_date`, `installment_number`) VALUES
(1, 100, 1, 3, 1, '2024-11-01', 1),
(2, 100, 1, 3, 1, '2024-12-01', 2),
(3, 25, 2, 3, 1, '2024-11-01', 1),
(4, 25, 2, 3, 1, '2024-12-01', 2),
(5, 125, 2, 4, 1, '2024-11-01', 1),
(6, 125, 2, 4, 1, '2024-12-01', 2),
(19, 750, 1, 2, 3, '2024-08-15', 1),
(20, 750, 1, 2, 3, '2024-09-15', 2),
(21, 16833.333333333332, 6, 2, 4, '2025-07-20', 1),
(22, 16833.333333333332, 6, 2, 4, '2025-08-20', 2),
(23, 16833.333333333332, 6, 2, 4, '2025-09-20', 3),
(24, 16666.666666666668, 6, 3, 4, '2025-07-20', 1),
(25, 16666.666666666668, 6, 3, 4, '2025-08-20', 2),
(26, 16666.666666666668, 6, 3, 4, '2025-09-20', 3),
(47, 750, 1, 5, 8, '2024-01-23', 1),
(48, 750, 1, 5, 8, '2024-02-23', 2),
(49, 750, 1, 6, 8, '2024-01-23', 1),
(50, 750, 1, 6, 8, '2024-02-23', 2),
(51, 14333.333333333334, 5, 8, 9, '2021-01-01', 1),
(52, 14333.333333333334, 5, 8, 9, '2021-02-01', 2),
(53, 14333.333333333334, 5, 8, 9, '2021-03-01', 3),
(54, 12333.333333333334, 6, 8, 9, '2021-01-01', 1),
(55, 12333.333333333334, 6, 8, 9, '2021-02-01', 2),
(56, 12333.333333333334, 6, 8, 9, '2021-03-01', 3),
(57, 3000, 5, 8, 10, '2025-06-26', 1),
(58, 7000, 6, 8, 10, '2025-06-26', 1),
(65, 5000, 1, 4, 11, '2024-07-10', 1),
(66, 5000, 1, 4, 11, '2024-08-10', 2),
(77, 3000, 3, 5, 12, '2025-04-01', 1),
(78, 3000, 3, 5, 12, '2025-05-01', 2),
(79, 3000, 3, 5, 12, '2025-06-01', 3),
(80, 3000, 3, 5, 12, '2025-07-01', 4),
(81, 3000, 3, 5, 12, '2025-08-01', 5),
(82, 3000, 3, 5, 12, '2025-09-01', 6),
(83, 2000, 9, 5, 12, '2025-04-01', 1),
(84, 2000, 9, 5, 12, '2025-05-01', 2),
(85, 2000, 9, 5, 12, '2025-06-01', 3),
(86, 2000, 9, 5, 12, '2025-07-01', 4),
(87, 2000, 9, 5, 12, '2025-08-01', 5),
(88, 2000, 9, 5, 12, '2025-09-01', 6);

-- Estructura de tabla para la tabla `expenses`
CREATE TABLE `expenses` (
  `id` int(11) NOT NULL,
  `amount` double NOT NULL CHECK (`amount` > 0),
  `date` date NOT NULL,
  `installments` int(11) NOT NULL CHECK (`installments` >= 1),
  `description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcado de datos para la tabla `expenses`
INSERT INTO `expenses` (`id`, `amount`, `date`, `installments`, `description`) VALUES
(1, 500, '2024-10-01', 2, 'Prueba 1'),
(2, 100000, '2025-06-20', 4, 'Casita'),
(3, 3000, '2024-07-15', 2, 'Parlante'),
(4, 100500, '2025-06-20', 3, 'Computadora'),
(5, 500678, '2025-03-15', 1, 'Celular'),
(7, 20000, '2025-05-01', 1, 'Reloj'),
(8, 3000, '2024-01-23', 2, 'Mate'),
(9, 80000, '2021-01-01', 3, 'comida'),
(10, 10000, '2025-06-26', 1, 'Plato'),
(11, 10000, '2024-07-10', 2, 'torta cumple benja'),
(12, 30000, '2025-04-01', 6, 'Guantes de invierno');

-- Estructura de tabla para la tabla `payments`
CREATE TABLE `payments` (
  `id` int(11) NOT NULL,
  `amount` double NOT NULL CHECK (`amount` > 0),
  `date` date NOT NULL,
  `payer_id` int(11) NOT NULL,
  `recipient_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcado de datos para la tabla `payments`
INSERT INTO `payments` (`id`, `amount`, `date`, `payer_id`, `recipient_id`) VALUES
(1, 1500, '2025-06-23', 1, 2),
(2, 2000, '2025-06-23', 1, 2),
(3, 2500, '2025-06-20', 2, 1),
(6, 400, '2025-06-24', 5, 3),
(7, 100, '2025-06-25', 5, 6),
(8, 2000, '2025-06-26', 6, 5),
(9, 80000, '2025-01-01', 8, 5),
(10, 1500, '2025-06-26', 5, 1),
(11, 4000, '2025-06-29', 5, 2);

-- Estructura de tabla para la tabla `users`
CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcado de datos para la tabla `users`
INSERT INTO `users` (`id`, `name`, `email`, `password`) VALUES
(1, 'lucas', 'lucas@example.com', 'clave123'),
(2, 'marina', 'marina@example.com', 'clave123'),
(3, 'tomas', 'tomas@example.com', 'clave123'),
(4, 'flor', 'flor@example.com', 'clave123'),
(5, 'maxi', 'maxi@gmail.com', '123'),
(6, 'daievs', 'dai@ejemplo.com', 'cuquito'),
(8, 'Cuba', 'cuba@gmail.com', '123'),
(9, 'catalina', 'cata@mail.com', 'cata');

-- Índices y claves foráneas
ALTER TABLE `debts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `creditor_id` (`creditor_id`),
  ADD KEY `debtor_id` (`debtor_id`),
  ADD KEY `expense_id` (`expense_id`);

ALTER TABLE `expenses`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `payer_id` (`payer_id`),
  ADD KEY `recipient_id` (`recipient_id`);

ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `email` (`email`);

-- AUTO_INCREMENT
ALTER TABLE `debts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=89;

ALTER TABLE `expenses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

ALTER TABLE `payments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

-- Restricciones
ALTER TABLE `debts`
  ADD CONSTRAINT `debts_ibfk_1` FOREIGN KEY (`creditor_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `debts_ibfk_2` FOREIGN KEY (`debtor_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `debts_ibfk_3` FOREIGN KEY (`expense_id`) REFERENCES `expenses` (`id`) ON DELETE CASCADE;

ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`payer_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
