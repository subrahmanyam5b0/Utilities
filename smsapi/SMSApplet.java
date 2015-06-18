/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smsapi;

import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import smsapi.panels.SMSPanels;
import utils.PortFinder;

/**
 *
 * @author USER
 */
public class SMSApplet extends javax.swing.JApplet implements Runnable {

    /**
     * Initializes the applet SMSApplet
     */
    public String comPath = "";
    public String propertiesPath = "";
    public String dllPath = "";
    JButton btnHome = new JButton("Home Page");
    private String host;
    private String homePage;
    private String params;
    private String contextRoot = "";
    public static boolean showAmount=false;
    public String defaultPort="";
    public String serviceCentres = "";
    public String collectionsForSMS = "";
    public String centresForSMS = "";
    public String saveSMS = "";
    public String centres = "";
    public ArrayList<String> allPorts = new ArrayList<String>();
    public static boolean failAttempts=false;
    public ArrayList<String> failedMessages=new ArrayList<String>(); 
    public ArrayList<ArrayList<String>> services = new ArrayList<ArrayList<String>>();
    public ArrayList<Model> ser = new ArrayList<Model>();
    public ArrayList<ArrayList<String>> collections = new ArrayList<ArrayList<String>>();
    public ArrayList<Model> col = new ArrayList<Model>();
    public ArrayList<ArrayList<String>> centresList = new ArrayList<ArrayList<String>>();
    public ArrayList<Model> cen = new ArrayList<Model>();
    
    public SMSPanels panels = new SMSPanels();
    public JLabel smsResult = new JLabel();
    public boolean chk = false;
    public Thread th = null;
    public SMSApplet app;
    ArrayList<String> result = new ArrayList<String>();

    @Override
    public void init() {
        System.setSecurityManager(null);
        try {
            app = this;

            this.setLayout(new BorderLayout());
            JScrollPane jscroll = new JScrollPane(panels, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            jscroll.setViewportView(panels);
            this.getContentPane().add(jscroll, BorderLayout.CENTER);
            setSize(1024, 590);
            try {
                URL base = getCodeBase();
                int port = base.getPort();
                if (port == -1) {
                    port = base.getDefaultPort();
                }
                contextRoot = base.getPath().trim();
                contextRoot = contextRoot.substring(0, contextRoot.indexOf('/', 1) + 1);
                this.host = (base.getProtocol() + "://" + base.getHost() + ":" + port);
                this.homePage = getParameter("homePage");
                if (contextRoot != null && this.homePage != null) {
                    this.homePage = this.homePage.replace("/" + contextRoot, "");
                }
                this.params = getParameter("params");
                this.serviceCentres = getParameter("serviceCentres");
                this.collectionsForSMS = getParameter("collections");
                this.centresForSMS = getParameter("centresForSMS");
                this.saveSMS = getParameter("saveSMS");
                if(getParameter("showAmount")!=null && getParameter("showAmount").equalsIgnoreCase("yes"))
                    showAmount=true;
                if(getParameter("failAttempts")!=null && getParameter("failAttempts").equalsIgnoreCase("yes"))
                    failAttempts=true;
                if(getParameter("defaultPort")!=null )
                    defaultPort=getParameter("defaultPort");
            } catch (Exception e) {
                try {
                    JOptionPane.showMessageDialog(this, "Exception :" + e.getMessage(), "Error", 1);
                    showDocument(homePage);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(this, " Error  While Showing Redirecting to Home Page :" + e.getMessage(), "Alert", 0);
                    throw e1;
                }
            }
//            params = "unionId=1&unitId=8570&routeId=-1&centreId=-1&date=2014-03-12&milkType=All&session=All";
//            contextRoot = "";
//            host = "http://localhost:8084/procurement/";
//            serviceCentres = "getServiceCentres.gbst";
//            collectionsForSMS = "getCollectionsForSMS.gbst";
//            centresForSMS = "getCentresForSMS.gbst";
//            saveSMS = "addSentSMS.gbst";

            comPath = host + contextRoot + "/files/comm.jar";
            propertiesPath = host + contextRoot + "/files/javax.comm.properties";
            dllPath = host + contextRoot + "/files/win32com.dll";

            try {
                new utils.CommunicationChecks().checkForCommAPI(comPath, propertiesPath, dllPath);
            } catch (Exception e) {
                if (System.getProperty("os.name").toLowerCase().contains("xp")) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error While Checking Files", 0);
                }
            }
            services = getPlainText(serviceCentres);
            collections = getPlainText(collectionsForSMS);
            centresList = getPlainText(centresForSMS);
            allPorts = new PortFinder().getAvailablePorts();
            if (allPorts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Connect a  3G Modem", "Error", 0);
            }

//             JOptionPane.showMessageDialog(this, " Service Centres:" + services.size(), "Message", 1);
//            JOptionPane.showMessageDialog(this, "  Centres:" + centresList.size(), "Message", 1);
//            JOptionPane.showMessageDialog(this, " Collections:" + collections.size(), "Message", 1);
//              JOptionPane.showMessageDialog(this, " ComPath:" + comPath, "Message", 1);
//              JOptionPane.showMessageDialog(this, " allPort:" + allPorts, "Message", 1);
            JPanel pnlButtons = new JPanel(new FlowLayout());

            pnlButtons.add(btnHome);

            JButton toggleSelection = new JButton("Toggle Selection");
            JButton getData = new JButton("Get-Data");
            JButton sendSMS = new JButton("Send SMS");
            pnlButtons.add(toggleSelection);
            //pnlButtons.add(getData);            
            pnlButtons.add(sendSMS);
            pnlButtons.add(smsResult);

            btnHome.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        showDocument(contextRoot + homePage);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Error While Redirecting To home Page" + e1.getMessage(), "Alert", 0);
                    }
                }
            });
            th = new Thread(app, "sms");
            //th.start();
            sendSMS.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        failedMessages=new ArrayList<String>();
                        chk = false;
                        th = new Thread(app, "sms");
                        if (allPorts.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please Connect a Modem", "Error", 0);
                            return;
                        }
                        if (panels.getjTabbedPane1().getSelectedIndex() == 3 && panels.getMessage().getText().length() == 0) {
                            JOptionPane.showMessageDialog(null, "No Message to Send", "Alert", 2);
                            return;
                        }
                        ArrayList<Integer> data = new ArrayList<Integer>();
                        if (panels.getjTabbedPane1().getSelectedIndex() == 0) {
                            for (int i = 0; i < panels.getCollectionTable().getRowCount(); i++) {
                                boolean chk = (Boolean) panels.getCollectionTable().getValueAt(i, 0);
                                if (chk) {
                                    data.add(i);
                                }
                            }
                        } else if (panels.getjTabbedPane1().getSelectedIndex() == 1) {
                            if (panels.getMessageText().getText().trim().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Please Enter Message", "Error", 0);
                                return;
                            }
                            for (int i = 0; i < panels.getOtherTable().getRowCount(); i++) {
                                boolean chk = (Boolean) panels.getOtherTable().getValueAt(i, 0);
                                if (chk) {
                                    data.add(i);
                                }
                            }
                        }
                        if (!data.isEmpty() || panels.getjTabbedPane1().getSelectedIndex() == 2 || panels.getjTabbedPane1().getSelectedIndex() == 3) {
                            String s = "Selected Rows :" + data.size() + "\n Are you Sure?";
                            if (panels.getjTabbedPane1().getSelectedIndex() == 2) {
                                if (panels.getSampleField().getText().length() != 10) {
                                    JOptionPane.showMessageDialog(null, "Please Enter Mobile Number", "Error", 0);
                                    return;
                                }
                                s = "Sample Message\n Are You Sure?";
                            } else if (panels.getjTabbedPane1().getSelectedIndex() == 3) {
                                if (panels.getToNumbers().getText().trim().length() < 10) {
                                    JOptionPane.showMessageDialog(null, "Please Enter Mobile Number(s)", "Error", 0);
                                    panels.getToNumbers().setText("");
                                    return;
                                }
                                s = "" + panels.getMessage().getText();
                            }
                            smsResult.setText("Please Wait");
                            int i = JOptionPane.showConfirmDialog(null, s, "Confirmation", 1);
                            if (i == 0) {
                                chk = false;
                                th.start();
                                result = new ArrayList<String>();
                                String centre = "+91";
                                if (panels.getOther().getText().length() > 0) {
                                    centre += panels.getOther().getText();
                                } else {
                                    centre = panels.getCentres().getSelectedItem().toString();
                                }
                                // System.out.println("Service Cent" + centre);
                                Sender.csca = centre;
                                MessageSender sd = new MessageSender();
                                Sender.port = ((Model) panels.getPorts().getSelectedItem()).getPort();
                                if (panels.getjTabbedPane1().getSelectedIndex() == 0) {
                                    int failCount = 0;
                                    for (Integer d : data) {
                                        Model _d = col.get(d);
                                        //System.out.print("Phone:" + _d.getPhone() + "\t Message:" + _d.getMessage()+"\t");
                                        if (!_d.getPhone().contains("+91")) {
                                            _d.setPhone("+91" + _d.getPhone());
                                        }
                                        //smsResult.setText("Records Processed :"+(data.indexOf(d)+1));
                                        String re = sd.sendMessage(_d.getPhone(), _d.getMessage());
                                        //System.out.println("Result:"+re);

                                        if (re.contains("sent")) {
                                            params = "";
                                            params = "centreId=" + _d.getCentreId() + "&date=" + _d.gettDate() + "&milkType=" + _d.getMilkType() + "&session=" + _d.getSession() + "&phone=" + _d.getPhone() + "&message=" + _d.getMessage();
                                            re = saveSMS(saveSMS);
                                            if (re.contains("Saved")) {
                                                result.add(_d.getCentreCode() + ",To:" + _d.getPhone() + "\tMsg:" + _d.getMessage());
                                            }
                                        } else {
                                            failCount++;
                                            failedMessages.add(_d.getCentreCode()+":"+_d.getPhone());
                                        }
                                        if (failCount == 3 && failAttempts) {
                                            JOptionPane.showMessageDialog(null, "Please Check Device/Balance. \nFail Attempts:" + failCount, "Alert", 2);
                                            break;
                                        }
                                    }
                                } else if (panels.getjTabbedPane1().getSelectedIndex() == 1) {
                                    int failCount = 0;

                                    for (Integer d : data) {
                                        Model _d = cen.get(d);
                                        if (!_d.getPhone().contains("+91")) {
                                            _d.setPhone("+91" + _d.getPhone());
                                        }
                                        // smsResult.setText("Records Processed :"+(data.indexOf(d)+1));
                                        String re = sd.sendMessage(_d.getPhone(), panels.getMessageText().getText());
                                        if (re.contains("sent")) {
                                            result.add(_d.getCentreCode() + ",To:" + _d.getPhone() + "\tMsg:" + panels.getMessageText().getText());
                                        } else {
                                            failCount++;
                                            failedMessages.add(_d.getCentreCode()+":"+_d.getPhone());
                                        }
                                        if (failCount == 3 && failAttempts) {
                                            JOptionPane.showMessageDialog(null, "Please Check Device/Balance. \nFail Attempts: " + failCount, "Alert", 2);
                                            break;
                                        }
                                        //System.out.println("Phone:" + _d.getPhone() + "\t Message:" +panels.getMessageText().getText() );
                                    }
                                } else if (panels.getjTabbedPane1().getSelectedIndex() == 2) {
                                    if (panels.getSampleField().getText().length() == 10) {
                                        String _result = sd.sendMessage(panels.getSampleField().getText(), "Sample Test Message");
                                        //JOptionPane.showMessageDialog(null,""+_result,"Reply",1);
                                        if (_result.contains("sent")) {
                                            result.add(_result);
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Please Enter Mobile Number");
                                    }
                                } else if (panels.getjTabbedPane1().getSelectedIndex() == 3) {
                                    {
                                        s = panels.getToNumbers().getText();
                                        StringTokenizer stk = new StringTokenizer(s, ",\n");

                                        while (stk.hasMoreTokens()) {
                                            s = stk.nextToken().trim();
                                            try {
                                                Long.parseLong(s);
                                            } catch (Exception _e) {
                                                _e.printStackTrace();
                                                continue;
                                            }
                                            if (!s.contains("+91")) {
                                                s = "+91" + s;
                                            }
                                            String temp = panels.getMessage().getText();

                                            if (temp.length() > 160) {
                                                String _t = temp.substring(0, 160);
                                                String _result = sd.sendMessage(s, _t);
                                                if (_result.contains("sent")) {
                                                    result.add(_result);
                                                }
                                                _t = temp.substring(160);
                                                _result = sd.sendMessage(s, _t);
                                                if (_result.contains("sent")) {
                                                    result.add(_result);
                                                }
                                            } else {
                                                String _result = sd.sendMessage(s, temp);
                                                if (_result.contains("sent")) {
                                                    result.add(_result);
                                                }
                                            }
                                        }
                                    }
                                }
                                chk = true;      
                                if(!failAttempts && !failedMessages.isEmpty()){
                                    String _s=failedMessages.toString();
                                    _s=_s.replace("[", "");
                                    _s=_s.replace("]", "");
                                    _s=_s.replace(",", "\n");
                                    JOptionPane.showMessageDialog(null, ""+_s, "Failed Messages", 0);
                                }
                                if (!result.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Message Successfully Send.   No. SMS:" + result.size(), "Message", 1);
                                } else if (!allPorts.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Please Check Device", "Error", 0);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please Select Rows:", "Error", 0);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error While Sending", "Error", 0);
                    }
                    smsResult.setText("");
                }
            });
            getData.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        //processData();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Error While Redirecting To home Page" + e1.getMessage(), "Alert", 0);
                    }
                }
            });
            toggleSelection.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // System.out.println("Called in Ac");
                    if (panels.getjTabbedPane1().getSelectedIndex() == 0) {
                        for (int i : panels.getCollectionTable().getSelectedRows()) {
                            panels.getCollectionTable().setValueAt(!(Boolean) panels.getCollectionTable().getValueAt(i, 0), i, 0);
                        }
                    } else if (panels.getjTabbedPane1().getSelectedIndex() == 1) {
                        for (int i : panels.getOtherTable().getSelectedRows()) {
                            panels.getOtherTable().setValueAt(!(Boolean) panels.getOtherTable().getValueAt(i, 0), i, 0);
                        }

                    }
                }
            });

            getContentPane().add(pnlButtons, BorderLayout.SOUTH);
            processData();

            //panels.tabone(collections);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private ArrayList<ArrayList<String>> getPlainText(String plainServlet) {
        HttpURLConnection updateConnection = null;
        // System.out.println("" + host + plainServlet);
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        try {
            URL updateURL = new URL(host + plainServlet);
            
            updateConnection = (HttpURLConnection) updateURL.openConnection();
            params += "";
            updateConnection.setDoOutput(true);
            BufferedOutputStream out = new BufferedOutputStream(updateConnection.getOutputStream());
            out.write(params.getBytes());
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(updateConnection.getInputStream()));
            String line = "";
            String response = "";
            while ((line = in.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                StringTokenizer stk = new StringTokenizer(line, ",");
                ArrayList<String> temp = new ArrayList<String>();
                while (stk.hasMoreTokens()) {
                    temp.add(stk.nextToken());
                }
                result.add(temp);
            }
            in.close();
        } catch (Exception e) {
            try {
                showDocument(homePage);
            } catch (Exception e1) {

            }
        }
        return result;
    }

    private String saveSMS(String plainServlet) {
        HttpURLConnection updateConnection = null;
        String response = "";
        // System.out.println("" + host + plainServlet);
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        try {
            URL updateURL = new URL(host + plainServlet);
            
            updateConnection = (HttpURLConnection) updateURL.openConnection();
            params += "";
            updateConnection.setDoOutput(true);
            BufferedOutputStream out = new BufferedOutputStream(updateConnection.getOutputStream());
            out.write(params.getBytes());
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(updateConnection.getInputStream()));
            String line = "";

            while ((line = in.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                response += line;
            }
            in.close();
        } catch (Exception e) {
            try {
                showDocument(homePage);
            } catch (Exception e1) {

            }
        }
        return response.trim();
    }

    public void initializeApplet() throws Exception {

    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(800, 500));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 848, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 422, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public void showDocument(String document) throws MalformedURLException {
        AppletContext context = getAppletContext();
        //JOptionPane.showMessageDialog(this, this.host+ contextRoot + document,"",0);
        context.showDocument(new URL(this.host + document));
    }

    public void showDocument1(String document) throws MalformedURLException {
        AppletContext context = getAppletContext();
        context.showDocument(new URL(this.host + document), "_blank");
    }

    public void processData() {
        ArrayList<Model> ports = new ArrayList<Model>();
        for (String s : allPorts) {
            Model dd = new Model();
            dd.setPort(s);
            ports.add(dd);
        }
        if (!ports.isEmpty()) {
            JComboBoxBinding jcb = SwingBindings.createJComboBoxBinding(AutoBinding.UpdateStrategy.READ, ports, panels.getPorts());
            jcb.bind();
            panels.getPorts().setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Model) {
                        Model mec = (Model) value;
                        setText(mec.getPort());
                    }
                    return this;
                }
            });
            if (ports.size() > 1 && System.getProperty("os.name").toLowerCase().contains("xp")) {
                panels.getPorts().setSelectedIndex(1);
            } else if (ports.size() > 6) {
                panels.getPorts().setSelectedIndex(5);
            } else {
                panels.getPorts().setSelectedIndex(0);
            }
            if(!defaultPort.isEmpty() && allPorts.contains(defaultPort) ){
                panels.getPorts().setSelectedIndex(allPorts.indexOf(defaultPort));
            }
        }
        ser = new ArrayList<Model>();
        for (ArrayList<String> s : services) {
            Model dd = new Model();
            dd.setServiceCentre(s.get(1));
            dd.setProvierName(s.get(0));
            ser.add(dd);
        }
        if (!ser.isEmpty()) {
            JComboBoxBinding jcb = SwingBindings.createJComboBoxBinding(AutoBinding.UpdateStrategy.READ, ser, panels.getSim());
            jcb.bind();
            panels.getSim().setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Model) {
                        Model mec = (Model) value;
                        setText(mec.getProvierName());
                    }
                    return this;
                }
            });
            panels.getSim().setSelectedIndex(0);
            ArrayList<String> sa = new ArrayList<String>();
            Model _sel = (Model) panels.getSim().getSelectedItem();
            for (Model d : ser) {
                if (d.getProvierName().equals(_sel.getProvierName())) {
                    sa.add(d.getServiceCentre());
                }
            }
            panels.getCentres().setModel(new DefaultComboBoxModel(sa.toArray()));
            panels.getCentres().setSelectedIndex(0);
        }
        panels.getSim().addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                ArrayList<String> sa = new ArrayList<String>();
                Model _sel = (Model) panels.getSim().getSelectedItem();
                for (Model d : ser) {
                    if (d.getProvierName().equals(_sel.getProvierName())) {
                        sa.add(d.getServiceCentre());
                    }
                }
                panels.getCentres().setModel(new DefaultComboBoxModel(sa.toArray()));
                panels.getCentres().setSelectedIndex(0);
            }
        });
        //processing for Collections SMS as ArrayList
        Object ds[][] = new Object[collections.size()][11];
        int colIndex = 0;
        panels.getMessage().setText("");
        String _sample = "";
        Model bm=new Model();
        Model cm=new Model();
        bm.setMilkType("BM");
        cm.setMilkType("CM");
        for (ArrayList<String> data : collections) {
            Model d = new Model();
            int i = 0;
            colIndex = 0;
            ds[collections.indexOf(data)][colIndex++] = true;
            d.setCentreId(Integer.parseInt(data.get(i++)));
            ds[collections.indexOf(data)][colIndex++] = data.get(i);
            d.setCentreCode(data.get(i++));
            ds[collections.indexOf(data)][colIndex++] = data.get(i);
            d.setCentreName(data.get(i++));
            ds[collections.indexOf(data)][colIndex++] = data.get(i);
            d.setRouteCode(data.get(i++));
            d.setRouteName(data.get(i++));
            ds[collections.indexOf(data)][colIndex++] = data.get(i);
            d.setUnitCode(data.get(i++));
            d.setUnitName(data.get(i++));
            bm.setUnitName(d.getUnitName());
            cm.setUnitName(d.getUnitName());
            d.settDate(data.get(i++));
            bm.settDate(d.gettDate());
            cm.settDate(d.gettDate());
            ds[collections.indexOf(data)][colIndex++] = data.get(i);
            d.setSession(data.get(i++));
            ds[collections.indexOf(data)][colIndex++] = data.get(i);
            d.setMilkType(data.get(i++));
            ds[collections.indexOf(data)][colIndex++] = data.get(i);
            d.setKgs(Double.valueOf(data.get(i++)));
            ds[collections.indexOf(data)][colIndex++] = data.get(i);
            d.setFat(Double.valueOf(data.get(i++)));
            ds[collections.indexOf(data)][colIndex++] = data.get(i);
            d.setSnf(Double.valueOf(data.get(i++)));
            d.setMilkValue(Double.valueOf(data.get(i++)));
            d.setNetValue(Double.valueOf(data.get(i++)));
            d.setBenfits(Double.valueOf(data.get(i++)));
            ds[collections.indexOf(data)][colIndex++] = data.get(i);
            d.setPhone(data.get(i++));            
            if(d.getMilkType().trim().equalsIgnoreCase("BM") && (collections.indexOf(data) != collections.size() - 1 && collections.indexOf(data) != collections.size() - 2)  ){                
               bm.setKgs(bm.getKgs()+d.getKgs());
               bm.setTotKgFat(bm.getTotKgFat()+d.getKgfat());
               bm.setTotKgSnf(bm.getTotKgSnf()+d.getKgsnf());
            }
            else if((collections.indexOf(data) != collections.size() - 1 && collections.indexOf(data) != collections.size() - 2)) {
                cm.setKgs(cm.getKgs()+d.getKgs());
                cm.setTotKgFat(cm.getTotKgFat()+d.getKgfat());
                cm.setTotKgSnf(cm.getTotKgSnf()+d.getKgsnf());
            }
            d.setTotKgFat(Double.valueOf(data.get(i++)));
            d.setTotKgSnf(Double.valueOf(data.get(i++)));            
            if (d.getCentreId() != 0) {
                col.add(d);
            } else if (collections.indexOf(data) == collections.size() - 1 || collections.indexOf(data) == collections.size() - 2) {
                if (d.getKgs() > 0) {
                    _sample += d.getMessage_unit() + ".";
                    //if(d.getMilkType().trim().equalsIgnoreCase("BM"))
                      //  _sample+=""+bm.getMessage_unit()+".";
                   // else
                      //  _sample+=""+cm.getMessage_unit()+".";
                }
            }
        }        
        panels.getMessage().setText(_sample);
        String co[] = new String[11];
        co[0] = "";
        co[1] = "Code";
        co[2] = "Name";
        co[3] = "Route Code";
        co[4] = "Unit Code";
        co[5] = "Session";
        co[6] = "Type";
        co[7] = "KGS";
        co[8] = "FAT";
        co[9] = "SNF";
        co[10] = "Phone";
        Object _sa[][] = new Object[ds.length - 2][11];
        for (int i = 0; i < ds.length - 2; i++) {
            if(ds[i][1]!=null && ds[i][1].toString().length()>0)
             _sa[i] = ds[i];
        }
        ds = _sa;
        panels.getCollectionTable().setModel(new javax.swing.table.DefaultTableModel(ds, co));

        ds = new Object[centresList.size()][7];

        cen = new ArrayList<Model>();
        for (ArrayList<String> data : centresList) {
            Model d = new Model();
            int i = 0;
            try {
                colIndex = 0;
                ds[centresList.indexOf(data)][colIndex++] = true;
                d.setCentreId(Integer.parseInt(data.get(i++)));
                ds[centresList.indexOf(data)][colIndex++] = data.get(i);
                d.setCentreCode(data.get(i++));
                ds[centresList.indexOf(data)][colIndex++] = data.get(i);
                d.setCentreName(data.get(i++));
                ds[centresList.indexOf(data)][colIndex++] = data.get(i);
                d.setPhone(data.get(i++));
                ds[centresList.indexOf(data)][colIndex++] = data.get(i);
                d.setRouteCode(data.get(i++));
                d.setRouteName(data.get(i++));
                ds[centresList.indexOf(data)][colIndex++] = data.get(i);
                d.setUnitCode(data.get(i++));
                d.setUnitName(data.get(i++));
                ds[centresList.indexOf(data)][colIndex++] = data.get(i);
                d.setPresident(data.get(i++));

            } catch (Exception e) {

            }
            cen.add(d);
        }
        co = new String[7];
        co[0] = "";
        co[1] = "Code";
        co[2] = "Name";
        co[3] = "Phone";
        co[4] = "Route Code";
        co[5] = "Unit Name";
        co[6] = "President";
        panels.getOtherTable().setModel(new javax.swing.table.DefaultTableModel(ds, co));
        TableColumn sportColumn = panels.getCollectionTable().getColumnModel().getColumn(0);
        sportColumn.setPreferredWidth(5);
        JCheckBox checkBox = new JCheckBox();
        sportColumn.setCellEditor(new DefaultCellEditor(checkBox));
        sportColumn.setCellRenderer(panels.getCollectionTable().getDefaultRenderer(Boolean.class));

        sportColumn = panels.getOtherTable().getColumnModel().getColumn(0);
        sportColumn.setPreferredWidth(5);
        checkBox = new JCheckBox();
        sportColumn.setCellEditor(new DefaultCellEditor(checkBox));
        sportColumn.setCellRenderer(panels.getOtherTable().getDefaultRenderer(Boolean.class));

        // bindCols();
        //bindCen();;
    }

    public void bindCen() {
        BindingGroup bindingGroup = new BindingGroup();
        JTableBinding jTableBinding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_WRITE, cen, panels.getOtherTable());
        JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${checked}"));
        columnBinding.setColumnName("");
        columnBinding.setColumnClass(Boolean.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${unitCode}"));
        columnBinding.setColumnName("Unit Code");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${routeCode}"));
        columnBinding.setColumnName("Route Code");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${centreCode}"));
        columnBinding.setColumnName("Code");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${centreName}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${president}"));
        columnBinding.setColumnName("President");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${phone}"));
        columnBinding.setColumnName("Phone");
        columnBinding.setColumnClass(String.class);

        bindingGroup.addBinding(jTableBinding);
        bindingGroup.bind();
    }

    public void bindCols() {
        BindingGroup bindingGroup = new BindingGroup();
        JTableBinding jTableBinding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_WRITE, col, panels.getCollectionTable());
        JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${checked}"));
        columnBinding.setColumnName("");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${routeCode}"));
        columnBinding.setColumnName("Route Code");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${centreCode}"));
        columnBinding.setColumnName("Code");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${centreName}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${phone}"));
        columnBinding.setColumnName("Phone");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${session}"));
        columnBinding.setColumnName("Session");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${milkType}"));
        columnBinding.setColumnName("Type");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${kgs}"));
        columnBinding.setColumnName("KGS");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${fat}"));
        columnBinding.setColumnName("FAT");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${snf}"));
        columnBinding.setColumnName("SNF");
        columnBinding.setColumnClass(String.class);

        bindingGroup.addBinding(jTableBinding);
        bindingGroup.bind();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void run() {
        if (!chk) {
            try {
                while (!chk) {
                    String s = smsResult.getText();
                    if (s.indexOf(".") == -1) {
                        s += ".";
                    } else if (s.substring(s.indexOf(".")).length() < 10) {
                        s += ".";
                    } else {
                        s = "Please Wait..";
                    }
                    smsResult.setText(s);
                    //System.out.println(""+s);
                    Thread.sleep(1000);
                }
                smsResult.setText("");
            } catch (Exception e) {
                smsResult.setText("");
            }
        } else {
            smsResult.setText("");
        }
    }
}
