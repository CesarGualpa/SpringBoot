package com.krakedev.clientes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.krakedev.clientes.entidades.Cliente;
import com.krakedev.clientes.services.ServicioCliente;

public class ServicioClienteTest {

	// PRUEBAS DEL METODO CREAR

	@Test
	public void crearClienteNuevoTest() {
		// Verificar que se puede crear un cliente nuevo con email.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");

		Cliente resultado = servicio.crear(cliente);

		assertEquals(cliente, resultado);
		assertEquals(1, servicio.listar().size());
		assertEquals("juan@email.com", resultado.getEmail());
	}

	@Test
	public void crearClienteDuplicadoTest() {
		// Verificar que no se puede crear un cliente cuando ya existe otro con la misma cedula.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente1 = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");
		Cliente cliente2 = new Cliente("1234567890", "Pedro", "Gomez", "pedro@email.com");

		servicio.crear(cliente1);

		Cliente resultado = servicio.crear(cliente2);

		assertNull(resultado);
		assertEquals(1, servicio.listar().size());
		assertEquals("juan@email.com", servicio.buscarPorCedula("1234567890").getEmail());
	}

	// PRUEBAS DEL METODO BUSCAR POR CEDULA

	@Test
	public void buscarPorCedulaExistenteTest() {
		// Verificar que se encuentra un cliente registrado y que conserva su email.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");

		servicio.crear(cliente);

		Cliente resultado = servicio.buscarPorCedula("1234567890");

		assertEquals(cliente, resultado);
		assertEquals("Juan", resultado.getNombre());
		assertEquals("juan@email.com", resultado.getEmail());
	}

	@Test
	public void buscarPorCedulaInexistenteTest() {
		// Verificar que se retorna null cuando no existe un cliente con la cedula indicada.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");

		servicio.crear(cliente);

		Cliente resultado = servicio.buscarPorCedula("9999999999");

		assertNull(resultado);
	}

	// PRUEBAS DEL METODO LISTAR

	@Test
	public void listarClientesTest() {
		// Verificar que se muestran todos los clientes registrados con su email.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente1 = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");
		Cliente cliente2 = new Cliente("0987654321", "Maria", "Lopez", "maria@email.com");

		servicio.crear(cliente1);
		servicio.crear(cliente2);

		List<Cliente> resultado = servicio.listar();

		assertEquals(2, resultado.size());

		assertTrue(resultado.contains(cliente1));
		assertTrue(resultado.contains(cliente2));

		assertEquals("juan@email.com", resultado.get(0).getEmail());
		assertEquals("maria@email.com", resultado.get(1).getEmail());
	}

	@Test
	public void listarClientesVacioTest() {
		// Verificar que la lista esta vacia cuando no se han registrado clientes.

		ServicioCliente servicio = new ServicioCliente();

		List<Cliente> resultado = servicio.listar();

		assertTrue(resultado.isEmpty());
		assertEquals(0, resultado.size());
	}

	// PRUEBAS DEL METODO ACTUALIZAR

	@Test
	public void actualizarClienteExistenteTest() {
		// Verificar que se actualizan nombre, apellido y email de un cliente existente.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");

		servicio.crear(cliente);

		Cliente clienteActualizado = new Cliente("1234567890", "Carlos", "Gomez", "carlos@email.com");

		Cliente resultado = servicio.actualizar("1234567890", clienteActualizado);

		assertEquals("Carlos", resultado.getNombre());
		assertEquals("Gomez", resultado.getApellido());
		assertEquals("1234567890", resultado.getCedula());
		assertEquals("carlos@email.com", resultado.getEmail());
	}

	@Test
	public void actualizarClienteInexistenteTest() {
		// Verificar que se retorna null cuando se intenta actualizar un cliente inexistente.

		ServicioCliente servicio = new ServicioCliente();

		Cliente clienteActualizado = new Cliente("9999999999", "Carlos", "Gomez", "carlos@email.com");

		Cliente resultado = servicio.actualizar("9999999999", clienteActualizado);

		assertNull(resultado);
		assertEquals(0, servicio.listar().size());
	}

	@Test
	public void actualizarClienteConservarCedulaTest() {
		// Verificar que la cedula original no cambia aunque el objeto actualizado tenga otra cedula.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");

		servicio.crear(cliente);

		Cliente clienteActualizado = new Cliente("9999999999", "Carlos", "Gomez", "carlos@email.com");

		Cliente resultado = servicio.actualizar("1234567890", clienteActualizado);

		assertEquals("1234567890", resultado.getCedula());
		assertEquals("Carlos", resultado.getNombre());
		assertEquals("Gomez", resultado.getApellido());
		assertEquals("carlos@email.com", resultado.getEmail());
	}

	@Test
	public void actualizarClienteEmailTest() {
		// Verificar especificamente que el email se actualiza correctamente.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");

		servicio.crear(cliente);

		Cliente clienteActualizado = new Cliente("1234567890", "Juan", "Perez", "nuevo@email.com");

		Cliente resultado = servicio.actualizar("1234567890", clienteActualizado);

		assertEquals("nuevo@email.com", resultado.getEmail());
	}

	// PRUEBAS DEL METODO ELIMINAR

	@Test
	public void eliminarClienteExistenteTest() {
		// Verificar que se elimina correctamente un cliente que se encuentra registrado.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");

		servicio.crear(cliente);

		boolean resultado = servicio.eliminar("1234567890");

		assertTrue(resultado);
		assertEquals(0, servicio.listar().size());
		assertNull(servicio.buscarPorCedula("1234567890"));
	}

	@Test
	public void eliminarClienteInexistenteTest() {
		// Verificar que se retorna false cuando se intenta eliminar un cliente inexistente.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");

		servicio.crear(cliente);

		boolean resultado = servicio.eliminar("9999999999");

		assertFalse(resultado);
		assertEquals(1, servicio.listar().size());
	}

	@Test
	public void eliminarClienteConservarOtrosClientesTest() {
		// Verificar que al eliminar un cliente, los demas clientes permanecen registrados con su email.

		ServicioCliente servicio = new ServicioCliente();

		Cliente cliente1 = new Cliente("1234567890", "Juan", "Perez", "juan@email.com");
		Cliente cliente2 = new Cliente("0987654321", "Maria", "Lopez", "maria@email.com");

		servicio.crear(cliente1);
		servicio.crear(cliente2);

		boolean resultado = servicio.eliminar("1234567890");

		assertTrue(resultado);
		assertEquals(1, servicio.listar().size());

		assertNull(servicio.buscarPorCedula("1234567890"));

		Cliente clienteRestante = servicio.buscarPorCedula("0987654321");

		assertEquals(cliente2, clienteRestante);
		assertEquals("maria@email.com", clienteRestante.getEmail());
	}
}