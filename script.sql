CREATE DATABASE IF NOT EXISTS `programa` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `programa`;

--
-- Estructura de tabla para la tabla `proveedores`
--
CREATE TABLE `proveedores` (
  `id_proveedor` int(2) NOT NULL,
  `nombre` varchar(10) NOT NULL,
  `fecha de alta` date NOT NULL,
  `id_cliente` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `proveedores`
--
INSERT INTO `proveedores` (`id_proveedor`, `nombre`, `fecha de alta`, `id_cliente`) VALUES
(1, 'Coca-cola', '2022-12-19', 5),
(2, 'Pepsi', '2022-12-19', 5),
(3, 'Redbull', '2022-12-19', 6);

--
-- Indices de la tabla `proveedores`
--
ALTER TABLE `proveedores` ADD PRIMARY KEY (`id_proveedor`);