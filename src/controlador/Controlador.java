/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Date;

import javax.swing.JOptionPane;
import vistas.JDialogAltaReserva;
import vistas.JDialogNoDisponible;
import vistas.ReservasJFramePrincipal;

/**
 * @author Angel FV
 * Implementa la interfaz ActionListener para manejar los eventos de acción de los botones 
 * y ventana
 * 
 * Este diseño centraliza el manejo de eventos en la clase Controlador, 
 * lo que permite gestionar múltiples ventanas y sus eventos desde un único lugar. 
 * La clave es hacer que el controlador registre los ActionListeners 
 * que se generen desde los componentes de las ventanas, de modo que pueda 
 * responder a los eventos de las ventanas.
 * 
 * Proyecto subido a github 8/10/24
 * Realizo otro cambio. otro más
 */
public class Controlador implements ActionListener, WindowListener{
    
    private ReservasJFramePrincipal vistaPrincipal;
    private JDialogAltaReserva dialogo_Modal_vista;
    private JDialogNoDisponible dialogo_Modal_no_disponible;
    
    int estadoDelDialogoDeLaReserva = 0;
    
    //* el constructor del controlador, recibe la ventana principal */
    public Controlador (ReservasJFramePrincipal ventanaPrincipal){
        
        this.vistaPrincipal = ventanaPrincipal;
        
        // Registramos el evento del botón en VentanaPrincipal 
        // registraremos todos los eventos que queramos atender de la ventana principal
        //desde el controlador
        this.vistaPrincipal.opcMenuReservasAltas.addActionListener(this);
        this.vistaPrincipal.opcMenuSalonesConsultar.addActionListener(this);
        this.vistaPrincipal.opcMenuOtrasAcercaDe.addActionListener(this); 
        
    }  
    
    public static void main(String[] args) {
        
        /* El main es el punto de inicio del programa donde  se crea la pantalla principal*/
        
        ReservasJFramePrincipal pantalla = new ReservasJFramePrincipal();
        pantalla.setAlwaysOnTop(true);
        pantalla.setSize(800,800); 
        // Centra la vista en la pantalla
        pantalla.setLocationRelativeTo(null);
        
        pantalla.setTitle("Menú principal");
        pantalla.setVisible(true); 
        
        /*creamos el objeto del constructor pasandole la pantalla principal*/
        Controlador controlador = new Controlador(pantalla);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        /*manejo eventos dela ventana principal*/ 
        /*
         Cuando el usuario pulsa el botón opcMenuReservasAltas, 
        este evento se recoge en este método del controlador que estará a la escucha y
        el controlador creara un new JDialog modal (JDialogAltaReserva) 
        y también asigna los  ActionListener a los botones correspondientes de esta nueva ventana
        para atender y gestionar los eventos que se generen en los componentes de la misma.
        */
         
         if (e.getSource() == vistaPrincipal.opcMenuReservasAltas) {
             
            /*Se atiende el evento de abrir la ventana del formulario de la reserva */
            /* la nueva ventana formulario de alta de reserva va a ser un jDialog modal
            Un diálogo modal bloquea la interacción con otras ventanas de la aplicación 
            mientras está abierto. El usuario no puede interactuar con ninguna otra ventana 
            hasta que cierre el diálogo modal.
            Para hacer que un JDialog sea modal, se debe especificar al crear el diálogo 
            o mediante el método setModal(true).*/
            
            
            if (dialogo_Modal_vista == null){
                dialogo_Modal_vista = new JDialogAltaReserva(this.vistaPrincipal, true);
                
                // nos apuntamos para escuchar los eventos para los botones de la ventana modal */
                dialogo_Modal_vista.btnReservar.addActionListener(this);
                
                //nos apuntamos para escuchar los eventos para las acciones sobre la ventana (cerrar, abrir...)
                dialogo_Modal_vista.addWindowListener(this);
                        /*
                    el boton btnReservarSinCierre, es simplemente para practicar, que al confirmar 
                    el alta de la reserva no me borre el formulario, sino que me lo siga dejando ver pero 
                    sin editarlo. mientras que el boton anterior btnReservar, cuando se muestre el mensaje de 
                    aviso al usuario de que la reserva ha sido realizada correctamente, pues en este caso 
                    si me elimina el formulario.
                    */
                dialogo_Modal_vista.btnReservarSinCierre.addActionListener(this);
                dialogo_Modal_vista.btnCancelar.addActionListener(this);
                dialogo_Modal_vista.setSize(600,600);
                dialogo_Modal_vista.setLocationRelativeTo(null);
                dialogo_Modal_vista.setTitle("Alta Reserva Salón Habana");
                
 /*               
                
                dialogo_Modal_vista.setUndecorated(true);

               
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());

               
                JLabel titulo = new JLabel("Alta Reserva Salón Habana", JLabel.CENTER);
                titulo.setFont(new Font("Arial", Font.BOLD, 24)); // Cambia la fuente y el tamaño
                titulo.setForeground(Color.BLUE); // Cambiar el color del texto si lo deseas

               
                panel.add(titulo, BorderLayout.NORTH);

               
                dialogo_Modal_vista.add(panel);
*/                
                
            }

            dialogo_Modal_vista.setVisible(true);
            
            /*cada vez que creo el formulario en un jdialog, inicializo a cero la vble 
               estadoDelDialogoDeLaReserva para el correcto control del joption panel informativo 
               con el resultado del procesamiento de la reserva.
            Esto ha sido necesario incluirlo porque sino me mostraba 2 veces el jpanel de aviso.
            */
            estadoDelDialogoDeLaReserva = 0;
            
        }
         
        if (dialogo_Modal_vista != null){
            if (e.getSource() == dialogo_Modal_vista.btnReservar) {
            /* la vble estadoDelDialogoDeLaReserva la usamos porque desde la ventana de dialogo vamos 
               a levantar un joptionpane y el botón "Aceptar" del JOptionPane dispara nuevamente el evento 
                    del botón "Reservar" del JDialog. 
            
                    Esto sucede porque, al ser ambos botones eventos de tipo ActionEvent, 
                    cuando presionas "Aceptar" en el JOptionPane, se vuelve a capturar 
                    el evento en el mismo ActionListener que gestiona el botón "Reservar".

                    Para solucionar este problema, se necesita diferenciar claramente 
                    el evento generado por el botón "Aceptar" del JOptionPane 
                    y el evento del botón "Reservar" del JDialog. 

                    Además, es importante asegurarse de que el evento del JOptionPane 
                    no propague nuevamente el evento de ActionPerformed.

            */
            
                if (estadoDelDialogoDeLaReserva == 0){

                    if (formulario_reserva_es_correcto()){
                        mostrar_mensaje_informativo_reserva_realizada();
                    }

                }
            }
        }
        
        /* el boton btnReservarSinCierre  lo usamos solo de ejemplo, de como quedaría la ventana 
           si en vez de cerrar el formulario, lo muestro vacío.
        */

        if (dialogo_Modal_vista != null){
            if (e.getSource() == dialogo_Modal_vista.btnReservarSinCierre) {
            
                if (formulario_reserva_es_correcto()){
                        mostrar_mensaje_informativo_reserva_realizada_sin_cerrar_el_formulario();
                }
            }
        }
        
        if (dialogo_Modal_vista != null){ 
            if (e.getSource() == dialogo_Modal_vista.btnCancelar) {
            
                /* variable que usamos para simular que el alta ha sido correcta en bbdd*/

                mostrar_mensaje_informativo_cancelar_reserva();
            }
        }
        
        if (e.getSource() == vistaPrincipal.opcMenuSalonesConsultar) {
            
             System.out.println("se captura el evento de pulsar el boton de consultar los salones");
            
            // Inicializa la vista Construcción solo si no está ya creada
            if (dialogo_Modal_no_disponible == null) { 
                
                System.out.println("se crea la pantalla jdialog de no disponible la opcion");
                
                dialogo_Modal_no_disponible = new JDialogNoDisponible(this.vistaPrincipal, true);
                dialogo_Modal_no_disponible.setSize(450,300);
                dialogo_Modal_no_disponible.setLocationRelativeTo(null);
                dialogo_Modal_no_disponible.setTitle("Opción no Disponible");
                
                System.out.println("se apunta como escuchador del boton advertencia");
                dialogo_Modal_no_disponible.btnAdvertencia.addActionListener(this);
            }
            
            System.out.println("se hace visible esta pantalla");
            dialogo_Modal_no_disponible.setVisible(true);
            

        }        
        
        if (dialogo_Modal_no_disponible != null){
            if (e.getSource() == dialogo_Modal_no_disponible.btnAdvertencia) {
            
                System.out.println("se captura el evento de pulsar el boton de aceptar el mensaje de advertencia");
                dialogo_Modal_no_disponible.dispose();
            }
        }
        
        if (e.getSource() == vistaPrincipal.opcMenuOtrasAcercaDe) {
            System.out.println("se captura el evento de pulsar el boton de Acerca de");
            
            // Inicializa la vista de advertencia solo si no está ya creada. es la misma vista de advertencia.
            if (dialogo_Modal_no_disponible == null) { 
                
                System.out.println("se crea la pantalla jdialog de no disponible la opcion");
                
                dialogo_Modal_no_disponible = new JDialogNoDisponible(this.vistaPrincipal, true);
                dialogo_Modal_no_disponible.setSize(450,300);
                dialogo_Modal_no_disponible.setLocationRelativeTo(null);
                dialogo_Modal_no_disponible.setTitle("Opción no Disponible");
                
                System.out.println("se apunta como escuchador del boton advertencia");
                dialogo_Modal_no_disponible.btnAdvertencia.addActionListener(this);
            }
            
            System.out.println("se hace visible esta pantalla");
            dialogo_Modal_no_disponible.setVisible(true); 
        }  
        
    }

    @Override
    public void windowOpened(WindowEvent e) {
     }

    @Override
    public void windowClosing(WindowEvent e) {
        

    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println("recojo el evento de cerrar la ventana modal");
        
        if (dialogo_Modal_vista != null){
            System.out.println("compruebo que existe la vista del dialogo");
            
            if (dialogo_Modal_vista.btnCancelar.isVisible()) {
                System.out.println("El botón Cancelar es visible.");
            } else {
                System.out.println("El botón Cancelar no es visible. por tanto estoy en la vista modal y he pulsado el boton de cerrar la ventana");
                dialogo_Modal_vista.dispose();
                dialogo_Modal_vista = null; // Esto permite que el recolector de basura elimine el objeto
            }
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
    

    
    public boolean formulario_reserva_es_correcto(){
        
        
        String textoCampo = dialogo_Modal_vista.txtNombre.getText().trim();
        if (textoCampo.isEmpty()) {           
            
            JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                "El campo de nombre no puede estar vacío", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            
            /*para que el foco se ponga en el campo que no supera las validaciones */
            dialogo_Modal_vista.txtNombre.requestFocus();
            return false;  // Salir si la validación falla

        }
        
         // Validar que el campo de teléfono contenga solo números
        String telefonoTexto = dialogo_Modal_vista.txtTelefono.getText().trim();
        
        if (telefonoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                "El campo de teléfono no puede estar vacío", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            
            dialogo_Modal_vista.txtTelefono.requestFocus();
            return false;  // Salir si la validación falla
        }
        
        // Verificar que el texto sea numérico
        try {
            Long.parseLong(telefonoTexto); // Usar Long si esperamos números largos
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                "El campo de teléfono debe contener solo números", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            
            dialogo_Modal_vista.txtTelefono.requestFocus();
            return false;  // Salir si la validación falla
        }
        
        // Validar que la fecha del JDateChooser sea mayor que la fecha actual 
        Date fechaSeleccionada = dialogo_Modal_vista.dateChooserFecha.getDate();
        if (fechaSeleccionada == null) {
            JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                "Debe seleccionar una fecha válida", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            
            dialogo_Modal_vista.dateChooserFecha.requestFocus();
            return false;  // Salir si la validación falla
        }
        
        Date fechaActual = new Date();
        if (fechaSeleccionada.before(fechaActual)) {
            JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                "La fecha seleccionada debe ser posterior a la fecha actual", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            
            dialogo_Modal_vista.dateChooserFecha.requestFocus();
            return false;  // Salir si la validación falla
        }
        
        String numeroAsistentes = dialogo_Modal_vista.txtNumeroAsistentes.getText().trim();
        
        if (numeroAsistentes.isEmpty()) {
            JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                "El campo numero de asistentes no puede estar vacío", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            
            dialogo_Modal_vista.txtNumeroAsistentes.requestFocus();
            return false;  // Salir si la validación falla
        }
        
        // Verificar que el texto sea numérico
        try {
            int numeroAsistentesInt = Integer.parseInt(numeroAsistentes); 
            
            if (numeroAsistentesInt <= 0){
               JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                    "El campo numero asistentes debe ser > 0", 
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
               
               dialogo_Modal_vista.txtNumeroAsistentes.requestFocus();
               return false;  // Salir si la validación falla
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                "El campo numero asistentes debe contener solo números", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            
            dialogo_Modal_vista.txtNumeroAsistentes.requestFocus();
            return false;  // Salir si la validación falla
        }
        
        // Validar que al menos un button group buttonGroupTipoEvento esté seleccionado 
        if (dialogo_Modal_vista.buttonGroupTipoEvento.getSelection() == null) {
            JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                "Debe seleccionar una opción de tipo de reserva", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            
            dialogo_Modal_vista.rBtnBanquete.requestFocus();
            return false;  // Salir si la validación falla
        }
        
        if (dialogo_Modal_vista.rBtnCongreso.isSelected()) {        
            // Validar que el valor del JSpinner sea mayor que 0 (ejemplo: número de asistentes)
            int numeroJornadas = (int) dialogo_Modal_vista.spinnerNumeroJornadas.getValue();
            if (numeroJornadas <= 0) {
                JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                    "El número de jornadas debe ser mayor que 0", 
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
                
                dialogo_Modal_vista.spinnerNumeroJornadas.requestFocus();
                return false;  
            }
        }
        
        // Validar que al menos un button group buttonGroupCocina esté seleccionado 
        if (dialogo_Modal_vista.buttonGroupCocina.getSelection() == null) {
            JOptionPane.showMessageDialog(dialogo_Modal_vista, 
                "Debe seleccionar una opción para la cocina del evento", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            
            dialogo_Modal_vista.rbnCarta.requestFocus();
            return false;  // Salir si la validación falla
        }
        
        return true;
        
    }
    
    public void mostrar_mensaje_informativo_reserva_realizada(){
        
        /*se simula que el proceso de alta ha sido correcto. 
          en esta practica no se tiene que realizar el acceso a bbdd */
        
        int resultado = 0;  

                System.out.println("se presiona el boton reservar en la venta del formulario "
                        + "de " + "la reserva");

                if (resultado == -1){   //lo dejo preparado por si huviera error en la persistencia

                      JOptionPane.showMessageDialog(this.dialogo_Modal_vista, 
                              "Error al procesar la Reserva", 
                              "Reserva NO REALIZADDA", 
                              JOptionPane.ERROR_MESSAGE);

                      dialogo_Modal_vista.dispose();
                      dialogo_Modal_vista = null; // Esto permite que el recolector de basura elimine el objeto
                }else{

                      System.out.println("la reserva ha sido correcta");

                    // con la variable estadoDelDialogoDeLaReserva = 1 controlamos
                    // la situación anterior y evitamos que se muester 2 veces el boton.
                    
                    
                    // otra forma de hacerlo sería no cerrar el dialogo padre y desabilitarlo
                    // para que mostrase el resumen de la reserva.

                       this.estadoDelDialogoDeLaReserva = 1;

                       // Texto del mensaje y título
                       String mensaje = "La reserva se ha realizado correctamente";
                       String titulo = "Reserva realizada";

                       // Creamos el botón personalizado
                        Object[] options = {"ACEPTAR"};

                        // Mostramos el JOptionPane con el botón "ACEPTAR"

                        
                        int seleccion = JOptionPane.showOptionDialog(
                            this.dialogo_Modal_vista,  // El JDialog padre
                            mensaje,  // El mensaje que queremos mostrar
                            titulo,  // El título del mensaje
                            JOptionPane.DEFAULT_OPTION,  // Tipo de opción
                            JOptionPane.INFORMATION_MESSAGE, // Tipo de mensaje (ícono de información)
                            null,  // Ícono personalizado (null para el ícono predeterminado)
                            options,  // Las opciones del botón
                            options[0]  // Opción por defecto (el primer botón)
                         );

                         // Si el usuario presiona "ACEPTAR" (posición 0 en el array de botones)

                         System.out.println("se ha pulsado el botón aceptar y debería seguir "
                                 + "desde este punto");

                        if (seleccion == 0) {
                            System.out.println("Botón ACEPTAR presionado");

                            // Cerrar el JDialog después de aceptar */
                            dialogo_Modal_vista.dispose();
                            dialogo_Modal_vista = null; // Esto permite que el recolector de basura elimine el objeto
                            
                            /* si pulso en el icono "x" cancelar el jpanel, hago lo mismo */
                        } else if (seleccion == JOptionPane.CLOSED_OPTION) {
                            dialogo_Modal_vista.dispose();
                            dialogo_Modal_vista = null; // Esto permite que el recolector de basura elimine el objeto
                        }

                }
    }

    private void mostrar_mensaje_informativo_reserva_realizada_sin_cerrar_el_formulario() {
        
            if (estadoDelDialogoDeLaReserva == 0){

                int resultado = 0;  

                System.out.println("se presiona el boton reservar en la venta del formulario "
                        + "de " + "la reserva");

                if (resultado == -1){
                        
                      JOptionPane.showMessageDialog(this.dialogo_Modal_vista, 
                              "Error al procesar la Reserva", 
                              "Reserva NO REALIZADDA", 
                              JOptionPane.ERROR_MESSAGE);

                      dialogo_Modal_vista.dispose();
                      dialogo_Modal_vista = null; // Esto permite que el recolector de basura elimine el objeto
                }else{

                      System.out.println("la reserva ha sido correcta");

                    /*
                        el botón "Aceptar" del JOptionPane dispara nuevamente el evento 
                    del botón "Reservar" del JDialog. 
                    Esto sucede porque, al ser ambos botones eventos de tipo ActionEvent, 
                    cuando presionas "Aceptar" en el JOptionPane, se vuelve a capturar 
                    el evento en el mismo ActionListener que gestiona el botón "Reservar".

                    Para solucionar este problema, se necesita diferenciar claramente 
                    el evento generado por el botón "Aceptar" del JOptionPane 
                    y el evento del botón "Reservar" del JDialog. 

                    Además, es importante asegurarse de que el evento del JOptionPane 
                    no propague nuevamente el evento de ActionPerformed.

                    */

                    // con la variable estadoDelDialogoDeLaReserva = 1 controlamos
                    // la situación anterior y evitamos que se muester 2 veces el boton.
                    
                    
                    // otra forma de hacerlo sería no cerrar el dialogo padre y desabilitarlo
                    // para que mostrase el resumen de la reserva.
                   
                       this.estadoDelDialogoDeLaReserva = 1;

                       // Texto del mensaje y título
                       String mensaje = "La reserva se ha realizado correctamente";
                       String titulo = "Reserva realizada";

                       Object[] options = {"ACEPTAR"};

                        
                        
                       // Mostrar el JOptionPane con el JDialog como ventana padre
                       int seleccion = JOptionPane.showOptionDialog(
                                dialogo_Modal_vista,   // El JDialog padre (ventana modal)
                                mensaje,               // El mensaje que queremos mostrar
                                titulo,                // El título del mensaje
                                JOptionPane.DEFAULT_OPTION,  // Tipo de opción
                                JOptionPane.INFORMATION_MESSAGE, // Tipo de mensaje (ícono de información)
                                null,                       // Ícono personalizado (null para el ícono predeterminado)
                                options,                    // Las opciones del botón
                                options[0]                  // Opción por defecto (el primer botón)
                        );
                            
                        if (seleccion == 0) {
                            System.out.println("Botón ACEPTAR presionado");
                            
                            /*
                                setAllComponentsEnabled(Container container, boolean enabled): 
                                Este método se encarga de recorrer todos los componentes que están 
                                dentro del JDialog (o cualquier otro contenedor como JPanel, etc.) 
                                y deshabilitarlos.
                            
                                getContentPane(): Para acceder a los componentes dentro del JDialog, 
                                se debe llamar a getContentPane(). 
                                Esto asegura que se esta  accediendo a todos los componentes 
                                que el JDialog contiene.
                            
                                Además de esta forma, todos los componentes dentro del contenedor, no
                                tienen poruqe estar definidos como publicos, pueden seguir siendo private.
                            */
                            
                            setAllComponentsEnabled(dialogo_Modal_vista.getContentPane(), false);
                           /*
                            
                            De esta forma, se necesita que el componente sea publico.
                            dialogo_Modal_vista.lblNombre.setEnabled(false);
                            dialogo_Modal_vista.txtNombre.setEnabled(false);
                           */
                           
                           /* no haría falta, pero es por si quiero que alguno ya no 
                              se muestre.   
                           */
                            dialogo_Modal_vista.btnCancelar.setVisible(false);
                            dialogo_Modal_vista.btnReservar.setVisible(false);
                            dialogo_Modal_vista.btnReservarSinCierre.setVisible(false);
                            
                        } else if (seleccion == JOptionPane.CLOSED_OPTION) {
                                   // El usuario cerró el JOptionPane con "X"
                                   System.out.println("El diálogo fue cerrado con 'X'.");
                                   
                                   setAllComponentsEnabled(dialogo_Modal_vista.getContentPane(), false);
                                   /*
                                     esto cerraría  también el JDialog si se presionó "X"
                                   dialogo_Modal_vista.dispose();  
                                   dialogo_Modal_vista = null; // Esto permite que el recolector de basura elimine el objeto
                                   */
                        }
                }  
            }
        
    }
        
    // Método para deshabilitar todos los componentes dentro de un JDialog 
    //o cualquier contenedor
    public void setAllComponentsEnabled(Container container, boolean enabled) {
        // Obtenemos todos los componentes del contenedor (que puede ser un JDialog, JPanel, etc.)
        Component[] components = container.getComponents();

        // Recorremos todos los componentes
        for (Component component : components) {
            component.setEnabled(enabled); // Deshabilitamos el componente

            // Si el componente es otro contenedor (como un JPanel), deshabilitamos sus hijos
            if (component instanceof Container) {
                setAllComponentsEnabled((Container) component, enabled);
            }
        }
    }

    private void mostrar_mensaje_informativo_cancelar_reserva() {
       
                       // Texto del mensaje y título
                       String mensaje = "La reserva se va a cancelar";
                       String titulo = "Cancelar reserva";

                       // Creamos el botón personalizado
                        Object[] options = {"ACEPTAR"};

                        // Mostramos el JOptionPane con el botón "ACEPTAR"

                        int seleccion = JOptionPane.showOptionDialog(
                            this.dialogo_Modal_vista,  // El JDialog padre
                            mensaje,  // El mensaje que queremos mostrar
                            titulo,  // El título del mensaje
                            JOptionPane.DEFAULT_OPTION,  // Tipo de opción
                            JOptionPane.INFORMATION_MESSAGE, // Tipo de mensaje (ícono de información)
                            null,  // Ícono personalizado (null para el ícono predeterminado)
                            options,  // Las opciones del botón
                            options[0]  // Opción por defecto (el primer botón)
                         );

                        if (seleccion == 0) {
                            System.out.println("Botón ACEPTAR presionado");

                            // Cerrar el JDialog después de aceptar */
                            dialogo_Modal_vista.dispose();
                            dialogo_Modal_vista = null; // Esto permite que el recolector de basura elimine el objeto
                            
                            /* si pulso en el icono "x" cancelar el jpanel, hago lo mismo */
                        } 
 
    }
    
}
