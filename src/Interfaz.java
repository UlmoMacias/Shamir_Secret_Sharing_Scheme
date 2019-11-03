import javax.swing.JOptionPane;

/**
* Clase Interfaz que muestra los 4 botones y el area de texo
*
*/
public class Interfaz extends javax.swing.JFrame{
    Cifrador cifrador = new Cifrador();
    Texto text = new Texto();

    /** 
    *Constructor de una interfaz
    */
    public Interfaz() {
       initComponents();
       this.setTitle("Proyecto 3 : ShamirSecretSharingScheme");
       this.setLocationRelativeTo(null);
       this.cmdOcultarMensaje.setEnabled(true);
       this.cmdOcultarMensajeDeTexto.setEnabled(true);
       this.cmdVerMensaje.setEnabled(true);
    }


    /** 
    * Metodo que inicia la interfaz
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel = new javax.swing.JPanel();
        cmdOcultarMensaje = new javax.swing.JButton();
        cmdOcultarMensajeDeTexto = new javax.swing.JButton();
        cmdVerMensaje = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtMensaje = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel.setLayout(new java.awt.GridLayout(3, 1, 10, 10));

        cmdOcultarMensaje.setText("Cifrar Mensaje");
        cmdOcultarMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOcultarMensajeActionPerformed(evt);
            }
        });
        jPanel.add(cmdOcultarMensaje);

        cmdOcultarMensajeDeTexto.setText("Cifrar Mensaje de Texto");
        cmdOcultarMensajeDeTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOcultarMensajeDeTextoActionPerformed(evt);
            }
        });
        jPanel.add(cmdOcultarMensajeDeTexto);

        cmdVerMensaje.setText("Descifrar Mensaje");
        cmdVerMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdVerMensajeActionPerformed(evt);
            }
        });
        jPanel.add(cmdVerMensaje);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jtMensaje.setColumns(20);
        jtMensaje.setRows(5);
        jScrollPane2.setViewportView(jtMensaje);

        
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        )
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                    )
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    /*
    *Metodo que oculta un mensaje desde el area de texto.
    */
    private void cmdOcultarMensajeActionPerformed(java.awt.event.ActionEvent evt) {
        requerimientos();
        cifrador.setNombreOriginal("NewText.txt");
        cifrador.cifrarMensaje(jtMensaje.getText());
    }

    /** 
    *Metodo que crea los dialogos de texto para el ingreso de requisitos.
    */
    private void requerimientos(){
        String res = JOptionPane.showInputDialog("Ingresa el nombre del archivo en el que seran guardadas las n evaluaciones del polinomio");
        cifrador.setNTAGNE(res);
        String res2 = JOptionPane.showInputDialog("Ingresa el numero total de evaluaciones requeridas (n > 2)");
        cifrador.setN(res2);
        String res3 = JOptionPane.showInputDialog("Ingresa el numero minimo de puntos necesarios para descifrar (1 < t <= n).");
        cifrador.setT(res3);
        String pass = JOptionPane.showInputDialog("Ingrese un password.");
        cifrador.setPassword(pass);
    }

    /*
    *Metodo que oculta un mensaje desde un archivo.
    */
    private void cmdOcultarMensajeDeTextoActionPerformed(java.awt.event.ActionEvent evt) {
       if(text.abrirArchivo() ){        
            requerimientos();
            cifrador.setNombreOriginal(text.getNombre());  
            cifrador.cifrarMensaje(text.getTexto());
       } 
    }

    /*
    *Metodo que revela un mensaje encriptado.
    */
    private void cmdVerMensajeActionPerformed(java.awt.event.ActionEvent evt) {
        Texto texto1=new Texto(); ;
        JOptionPane.showMessageDialog(null, "Ingrese el texto con el mensaje encryptado (terminacion aes)"); 
        if (texto1.abrirArchivo()) {
            cifrador.setNombreCifrado(texto1.getNombre());
            String b = texto1.getTexto();
            JOptionPane.showMessageDialog(null, "Ingrese el texto con las evaluaciones suficientes para revelar el mensaje"); 
            if(text.abrirArchivo() ){ 
                cifrador.setNombreEvaluaciones(text.getNombre());       
                cifrador.descifrarMensaje(text.getTexto(),b);    
            }
        }
    }

    /*
    *Metodo main de una interfaz
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    private javax.swing.JButton cmdOcultarMensaje;
    private javax.swing.JButton cmdOcultarMensajeDeTexto;
    private javax.swing.JButton cmdVerMensaje;
    private javax.swing.JPanel jPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jtMensaje;

}