package com.jettytest;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;
import java.util.Vector;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
public class WebInterface extends Application implements TabSheet.SelectedTabChangeListener  {
    
	private TabSheet t;
    private Tab simulation, templates;
    private int tabstateS = 0;
    private int tabstateT = 0;
    private VerticalLayout layout, t1, t2;
    private GridLayout passLayout, formLayout;
    Window main, popup, passreq;
    Table table, passengers;
    int id;
    Passenger pass;
    Template bean;
    boolean random, temp;
    Update upd;

    @Override
    public void init() {
    //EFFECTS: an  instance of WebInterface is initiated
        TabLayout();
    }
   
    private void TabLayout() {
    //EFFECTS: a new tabsheet is created with two tabs - "Manage Templates" and "Manage Simulation"
    	layout = new VerticalLayout();
        
    	//Main window: 
    	main = new Window("Web Interface", layout); 
    	setMainWindow(main);
        
        // Tab 1 content:
    	t1 = new VerticalLayout();
    	t1.setSpacing(true);
        t1.setMargin(true);
        
        // Tab 2 content:
        t2 = new VerticalLayout();
        t2.setSpacing(true);
        t2.setMargin(true);
        
        //Creating tabsheet:
        t = new TabSheet();
        t.setHeight("630px");
        t.setWidth("1000px");

        //Adding tabs to tabsheet:
        templates = t.addTab(t1, "Manage Templates", null);
        simulation = t.addTab(t2, "Manage Simulation", null);
        t.addListener(this); //listens for change in tabs

        layout.addComponent(t);
        TemplateView(); //setting the initial user view
    }

    public void selectedTabChange(SelectedTabChangeEvent event) {
    //EFFECTS: the view is set based on the selection of tabs and the state each tab was last in
    	
        TabSheet tabsheet = event.getTabSheet();
        
        Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
        if(tab == templates && tabstateT == 0)
        	TemplateView();
        if(tab == templates && (tabstateT == 1 || tabstateT == 2))
        	CreateTemplateView();
        //CHECK HERE IF SIM IS RUNNING AND SET THE VALUE OF TABSTATES ACCORDINGLY
        //tabstateS = 0; //simulation is running
        //tabstateS = 1; //simulation is not running
        if(tab == simulation && tabstateS == 0)
        	SimulationStartView();
        if(tab == simulation && tabstateS == 1)
        	SimulationStopView();
    }
       
    private void TemplateView(){
    //EFFECTS: the user is taken to the window that displays all the currently existing templates
    //         as well as the option to create a new template
    	
    	t1.removeAllComponents(); //clearing the tab
    	t1.setHeight("580px");
        
        final Form frame = new Form(); //used as border
        frame.setCaption("Manage Templates");
        
        //Option to create new template:
    	Button CreateTemplate = new Button("Create Template", new Button.ClickListener() {
        	public void buttonClick(ClickEvent event) {               	
        		tabstateT = 1;
                CreateTemplateView();
            }
        });       

        final Form space = new Form(); //used as border
        space.setCaption("Existing Templates");   

        //Table of existing templates:
        table = new Table(); 
        table.setWidth("100%");
        table.setHeight("450px"); 
        table.addContainerProperty("Name", String.class,  null);
        table.addContainerProperty("Created", String.class,  null);
        table.addContainerProperty("Last Edited", String.class, null);
        table.addContainerProperty("Edit", Button.class, null);
        table.addContainerProperty("Delete", Button.class, null);
                     
        //Populating the table: (WILL BE MODIFIED)
        for (int i=0; i<3; i++) {
        	//Table fields for the row:
            Label name = new Label("Template" + i);
            Label datecreated = new Label("September 10, 2010");
            Label dateedited = new Label("September 11, 2010");
                
            //Table item identifier for the row:
            final Integer itemId = new Integer(i);
                
            Button edit = new Button("Edit");
            edit.setData(itemId);
            edit.addListener(new Button.ClickListener() { 
            	public void buttonClick(ClickEvent event) { 
            		/////////////////////////////////////////////////////
                } 
            });
                
            Button delete = new Button("Delete");
            delete.setData(itemId);
            delete.addListener(new Button.ClickListener() {  
            	public void buttonClick(ClickEvent event) {
            		popup(itemId); 
                } 
            });
            
            table.addItem(new Object[] {name, datecreated,dateedited, edit, delete}, itemId);    
        }

        //Adding components to tab:
        t1.addComponent(frame);
        t1.addComponent(CreateTemplate);
        t1.addComponent(space);
        t1.addComponent(table);
    }
    
    private void popup(int itemId){
    //REQUIRED: itemId != null	
    //EFFECTS: a window pops up that warns the user about the consequences of deleting a 
    //         template and allows user to either proceed with or cancel deletion
    	
    	id = itemId;
    	
    	//Defining popup window attributes and content:
        popup = new Window("Warning!");
        popup.setModal(true);
        popup.setHeight("100px");
        popup.setWidth("320px");
        popup.setResizable(false);
        popup.setClosable(false);
        
        Label warning = new Label("Are you sure you would like to delete this template?");
        VerticalLayout pl = new VerticalLayout();
        pl.setSpacing(true);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        
        Button yes = new Button("Yes", new Button.ClickListener(){
        	public void buttonClick(ClickEvent event){
        		table.removeItem(id);
        		main.removeWindow(popup);
        	}
        });
        
        Button no = new Button("No", new Button.ClickListener(){
        	public void buttonClick(ClickEvent event){
        		main.removeWindow(popup);
        	}
        });

        //Adding content to popup:
        buttons.addComponent(yes);
        buttons.addComponent(no);
        pl.addComponent(warning);
        pl.addComponent(buttons);
        popup.addComponent(pl);
        main.addWindow(popup);
    }
       
    private void AddPassenger(){
    //EFFECTS: a window pops up that contains the form for specifying passenger requests	
    	
    	//Defining passreq window attributes and content:
        passreq = new Window("Passenger Request Form");
        passreq.setModal(true);
        passreq.setHeight("100px");
        passreq.setWidth("320px");
        passreq.setResizable(false);
        passreq.setClosable(false);
               
        VerticalLayout pl = new VerticalLayout();
        pl.setSpacing(true);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        
        //Creating the form for defining passenger requests:
        final Form form = new Form();
    	pass = new Passenger();
    	BeanItem item = new BeanItem(pass);
    	form.setItemDataSource(item);
    	form.setFormFieldFactory(new MyFieldFactory2());
    	form.setWriteThrough(false);
    	Vector order = new Vector();
    	order.add("time");
    	order.add("pfloor");
    	order.add("dfloor");
    	order.add("constraint");
    	form.setVisibleItemProperties(order);
        
        Button save = new Button("Save", new Button.ClickListener(){
        	public void buttonClick(ClickEvent event){
    			int saved;
    			try{
    				form.commit();
    				saved = 1;
    			} catch(Exception e){
    				saved = 0;
    			}
    			if(saved == 1){
    				main.removeWindow(passreq);
    			}
        	}
        });
        
        Button cancel = new Button("Cancel", new Button.ClickListener(){
        	public void buttonClick(ClickEvent event){
        		main.removeWindow(passreq);
        	}
        });
         	      
        //Adding content to passreq window:
        buttons.addComponent(save);
        buttons.addComponent(cancel);
        pl.addComponent(buttons);
        passreq.addComponent(pl);
        main.addWindow(passreq);
    }
   
    private void CreateTemplateView(){
    //EFFECTS: the user is taken to the window that allows for creation of a new template	
    	
    	t1.removeAllComponents(); //clearing the tab
    	t1.setHeight("800px");
    	
    	//Setting up the form for defining template:
    	final Form form = new Form();
    	form.setCaption("Form Caption");
    	
    	//Creating the data source for the form:
    	bean = new Template();
    	BeanItem item = new BeanItem(bean);
       	form.setItemDataSource(item);
    	form.setFormFieldFactory(new MyFieldFactory1());
    	form.setWriteThrough(false);
    	Vector order = new Vector();
    	order.add("name");
    	order.add("floors");
    	order.add("elevators");
    	order.add("algorithm");
    	form.setVisibleItemProperties(order);
    	
    	formLayout = new GridLayout(2, 2);
    	formLayout.setMargin(true, false, false, true);
    	formLayout.setSpacing(true);
    	form.setLayout(formLayout);
    	
    	//Creating other components for the page that are not part of the form:
    	HorizontalLayout buttons = new HorizontalLayout();
    	buttons.setSpacing(true);
    	
    	CheckBox random = new CheckBox("Generate random passenger requests", new CheckBox.ClickListener(){
    		public void buttonClick(ClickEvent event){
    			boolean enable = event.getButton().booleanValue();
    			bean.setRandom(enable);
    		}
    	});
    	
    	Button request = new Button("Add Passenger Request", new Button.ClickListener(){
    		public void buttonClick(ClickEvent event){
    			AddPassenger();
    		}
    	});
    	
    	//Creating table of passenger requests:
    	passengers = new Table();
        passengers.setWidth("100%");
        passengers.setHeight("300px"); 
        passengers.addContainerProperty("Time", String.class,  null);
        passengers.addContainerProperty("Pick Up Floor", Integer.class,  null);
        passengers.addContainerProperty("Drop Off Floor", Integer.class, null);
        passengers.addContainerProperty("Timing Constraint", Integer.class, null);
        passengers.addContainerProperty("Delete", Button.class, null);
    	
        Button save = new Button("Save", new Button.ClickListener(){
    		public void buttonClick(ClickEvent event){
    			int saved;
    			try{
    				form.commit();
    				saved = 1;
    			} catch(Exception e){
    				saved = 0;
    			}
    			if(saved == 1){
    				tabstateT = 0;
        			TemplateView();
    			}
    		}
    	});
        
        Button cancel = new Button("Cancel", new Button.ClickListener(){
    		public void buttonClick(ClickEvent event){
    			tabstateT = 0;
    			TemplateView();
    		}
    	});	
    	
        //Adding components to the tab:
    	buttons.addComponent(save);
    	buttons.addComponent(cancel);
    	t1.addComponent(form);
    	t1.addComponent(random);
    	t1.addComponent(request);
    	t1.addComponent(passengers);
    	t1.addComponent(buttons);
    }
               
    Simulation sim;
    
    private void SimulationStartView(){
    //EFFECTS: the window displays the option for user to start a new simulation	
    	t2.removeAllComponents();
    	t2.setHeight("500px");
        
        final Form form = new Form(); //used as border
        form.setCaption("Start Simulation");
        
        //Creating the data source for the form:
    	sim = new Simulation();
    	BeanItem item = new BeanItem(sim);
       	form.setItemDataSource(item);
    	form.setFormFieldFactory(new MyFieldFactory3());
    	form.setWriteThrough(false);
    	Vector order = new Vector();
    	order.add("name");
    	order.add("template");
    	form.setVisibleItemProperties(order);
    	
    	GridLayout layout = new GridLayout(1, 2);
    	layout.setSpacing(true);
    	layout.setMargin(true, true, true, true);
    	form.setLayout(layout);
        
    	//Creating the button to start simulation
    	Button start = new Button("Start Simulation", new Button.ClickListener() {
        	public void buttonClick(ClickEvent event) {               	
        		int saved;
        		try{
        			form.commit();
        			saved = 1;
        		}catch(Exception e){
        			saved = 0;
        		}
        		if(saved == 1){
        			tabstateS = 2;
        			//random = sim.getTemplate().getRandom();
        			random = true;
                	SimulationStopView();
        		}
            }
        }); 
    	
    	//Adding the form to the layout:
    	form.getFooter().addComponent(start);
        t2.addComponent(form);
    }
    
    private void SimulationStopView(){
    //EFFECTS: the window displays the option to stop or modify the current simulation	
    	t2.removeAllComponents();
    	t2.setHeight("600px");
    	
    	//Option to stop simulation:
    	final Form stopsim = new Form(); //used as border
        stopsim.setCaption("Stop Simulation");
        
        //Stop button:
        Button stop = new Button("Stop Simulation", new Button.ClickListener(){
    		public void buttonClick(ClickEvent event){
    			//SAVE SIMULATION RESULTS
    			tabstateS = 0;
        		SimulationStartView();
    		}
    	});
        
        //Setting the layout of the stop option:
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true, true, true, true);
        layout.setSpacing(true);
        layout.addComponent(stop);
        stopsim.setLayout(layout);
        
        //Option to edit simulation settings:
        final Form settings = new Form();
        settings.setCaption("Simulation Settings");
        
    	upd = new Update();
    	BeanItem item = new BeanItem(upd);
       	settings.setItemDataSource(item);
    	settings.setFormFieldFactory(new MyFieldFactory4());
    	settings.setWriteThrough(false);
    	Vector order = new Vector();
    	order.add("algorithm");
    	order.add("time");
    	order.add("distance");
    	order.add("limit");
    	settings.setVisibleItemProperties(order);
    	
    	//Setting the layout of the settings option:
       	VerticalLayout layout2 = new VerticalLayout();
    	layout2.setSpacing(true);
    	layout2.setMargin(true, true, true, true);
    	settings.setLayout(layout2);

    	//Random passenger generation option for the footer:
    	CheckBox rand = new CheckBox("Generate random passenger requests");
    	rand.setValue(random);
    	rand.addListener(new ClickListener(){
    		public void buttonClick(ClickEvent event){
    			temp = event.getButton().booleanValue();
    		}
    	});

        //Update button:
    	Button update = new Button("Update Simulation", new Button.ClickListener() {
        	public void buttonClick(ClickEvent event) {               	
        		try{
        			settings.commit();
        			random = temp;
        			upd.setRandom(random);
        			getMainWindow().showNotification("Simulation Updated", Notification.TYPE_WARNING_MESSAGE);
        		}catch(Exception e){
        		}
            }
        }); 
    	
    	//Setting layout of the footer:
    	VerticalLayout lf = new VerticalLayout();
    	lf.setSpacing(true);
    	lf.setMargin(false, false, true, true);
    	lf.addComponent(rand);
      	lf.addComponent(update);
    	settings.setFooter(lf);
        
    	//Add simulation events option:
        final Form events = new Form();
        events.setCaption("Simulation Events");
        
        //Setting the layout of the simulation events option:
        GridLayout grid = new GridLayout(8,3);
        grid.setSpacing(true);
        grid.setMargin(true, true, true, true);
        events.setLayout(grid);
        
        //Need to know number of floors and elevators used for simulation:
        //int floors = sim.getTemplate().getFloors()
        //int elevs = sim.getTemplate().getElevators()
        //for now let's set floors to 10 and elevs to 5:
        int floors = 10;
        int elevs = 5;
        
        //First line:
        Button insert1 = new Button("Insert");
        final ComboBox from = new ComboBox();
        for(int i = 1; i<=floors; i++){
        	from.addItem(i);
        }
        from.setValue(1);
        from.setNullSelectionAllowed(false);
        from.setWidth("4em");
        
        final ComboBox to = new ComboBox();
        for(int i = 1; i<=floors; i++){
        	to.addItem(i);
        	to.setValue(i);
        }
        to.setNullSelectionAllowed(false);
        to.setWidth("4em");
        
        final TextField quantum1 = new TextField();
        quantum1.setWidth("8em");
        
        //Insert event:
        insert1.addListener(new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				int test = 1;
				int x = 0;
				try{
					x = Integer.parseInt(quantum1.getValue().toString());
				}catch(NumberFormatException nFE){
					test = 0;
				}
				
				if(to.getValue() == from.getValue()){			
					getMainWindow().showNotification(
                            "Insertion Failed: Origin and destination floors cannot be the same.",
                            Notification.TYPE_ERROR_MESSAGE);
				}else if(test == 0 || x>1000){
					getMainWindow().showNotification("Insertion Failed: Time must be an integer no greater than 1000.", Notification.TYPE_ERROR_MESSAGE);
				}else if(quantum1.getValue().toString().substring(0,1).equals("-")){
					getMainWindow().showNotification("Insertion Failed: Time cannot be negative.", Notification.TYPE_ERROR_MESSAGE);
				}else{
					getMainWindow().showNotification("Insertion Successful", Notification.TYPE_WARNING_MESSAGE);
					System.out.println("Insert passenger request from floor " + from.getValue() + " to " + to.getValue() + " in " + quantum1.getValue() + " time quanta.");
				}
			}
		});
        
        grid.addComponent(insert1);
        grid.addComponent(new Label("Insert passenger request from floor"));
        grid.addComponent(from);
        grid.addComponent(new Label("to floor"));
        grid.addComponent(to);
        grid.addComponent(new Label("in"));
        grid.addComponent(quantum1);
        grid.addComponent(new Label("time quanta."));
        
        //Second line:
        Button insert2 = new Button("Insert");
        
        final ComboBox components = new ComboBox();
        components.addItem("F1 - Sensor");
        components.addItem("F2 - Sensor");
        components.addItem("Main Light");
        components.setValue("F1 - Sensor");
        components.setNullSelectionAllowed(false);
        components.setWidth("9em");
        
        final ComboBox elev1 = new ComboBox();
        for(int i = 1; i<=elevs; i++){
        	elev1.addItem(i);
        }
        elev1.setValue(1);
        elev1.setNullSelectionAllowed(false);
        elev1.setWidth("4em");
        
        final TextField quantum2 = new TextField();
        quantum2.setWidth("8em");
        
        //Insert event:
        insert2.addListener(new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				int test = 1;
				int x = 0;
				try{
					x = Integer.parseInt(quantum2.getValue().toString());
				}catch(NumberFormatException nFE){
					test = 0;
				}
				if(test == 0 || x > 1000){
					getMainWindow().showNotification("Insertion Failed: Time must be an integer no greater than 1000.", Notification.TYPE_ERROR_MESSAGE);
				}else if(quantum2.getValue().toString().substring(0,1).equals("-")){
					getMainWindow().showNotification("Insertion Failed: Time cannot be negative.", Notification.TYPE_ERROR_MESSAGE);
				}else{
					getMainWindow().showNotification("Insertion Successful", Notification.TYPE_WARNING_MESSAGE);
					System.out.println("Insert failure of " + components.getValue() + " for elevator " + elev1.getValue() + " in " + quantum2.getValue() + " time quanta.");
				}
			}
		});
        
        grid.addComponent(insert2);
        grid.addComponent(new Label("Insert failure of"));
        grid.addComponent(components);
        grid.addComponent(new Label("for elevator"));
        grid.addComponent(elev1);
        grid.addComponent(new Label("in"));
        grid.addComponent(quantum2);
        grid.addComponent(new Label("time quanta."));
        
        //Third line:
        Button insert3 = new Button("Insert");
        
        final ComboBox choice = new ComboBox();
        choice.addItem("out of");
        choice.addItem("into");
        choice.setValue("out of");
        choice.setNullSelectionAllowed(false);
        choice.setWidth("6em");
        
        final ComboBox elev2 = new ComboBox();
        for(int i = 1; i<=elevs; i++){
        	elev2.addItem(i);
        }
        elev2.setValue(1);
        elev2.setNullSelectionAllowed(false);
        elev2.setWidth("4em");
        
        final TextField quantum3 = new TextField();
        quantum3.setWidth("8em");
       
        //Insert event:
        insert3.addListener(new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				int test = 1;
				int x = 0;
				try{
					x = Integer.parseInt(quantum3.getValue().toString());
				}catch(NumberFormatException nFE){
					test = 0;
				}
				if(test == 0 || x>1000){
					getMainWindow().showNotification("Insertion Failed: Time must be an integer no greater than 1000.", Notification.TYPE_ERROR_MESSAGE);
				}else if(quantum3.getValue().toString().substring(0,1).equals("-")){
					getMainWindow().showNotification("Insertion Failed: Time cannot be negative.", Notification.TYPE_ERROR_MESSAGE);
				}
				else{
					getMainWindow().showNotification("Insertion Successful", Notification.TYPE_WARNING_MESSAGE);
					System.out.println("Force elevator " + elev2.getValue() + " " + choice.getValue() + " service in " + quantum3.getValue() + " time quanta.");
				}
			}
		});
        
        grid.addComponent(insert3);
        grid.addComponent(new Label("Force elevator"));
        grid.addComponent(elev2);
        grid.addComponent(choice);
        grid.addComponent(new Label("service"));
        grid.addComponent(new Label("in"));
        grid.addComponent(quantum3);
        grid.addComponent(new Label("time quanta."));
        
        //Adding the three options to he layout:
        t2.addComponent(stopsim);
        t2.addComponent(settings);
        t2.addComponent(events);
    }

    public class MyFieldFactory1 implements FormFieldFactory{
        //OVERVIEW: MyFieldFactory1 is used to create, validate and define fields for the form 
        //          from the CreateTemplateView() method	
        	
        	public Field createField(Item item, Object propertyId, Component uiContext){
        	//EFFECTS: fields for the form are displayed and inputs are validated	
        		
        		String pid = (String) propertyId;
        		//Setting Template Name field:
        		if(pid.equals("name")){
        			TextField name = new TextField("Template Name:");
        			name.setRequired(true);
        			name.setRequiredError("Please enter a valid template name.");
        			name.setWidth("15em");
        			name.addValidator(new StringLengthValidator("Template name must be shorter than 20 characters", 1, 22, false));
        			return name;
        		}
        		//Setting Number of Floors field:
        		else if (pid.equals("floors")){
        			Select floor = new Select("Number of Floors:");
        			for(int i = 1; i<= 50; i++)
        				floor.addItem(i);
        			floor.setRequired(true);
        			floor.setRequiredError("Please enter the number of floors.");
        			floor.setWidth("15em");
        			Validator floorValid = new Validator(){
    					public void validate(Object value)
    							throws InvalidValueException {
    						if(!isValid(value)){
    							throw new InvalidValueException("Please enter the number of floors.");	
    						}
    					}
    					public boolean isValid(Object value) {
    						if(value.toString().equals("0")){
    							return false;
    						}
    						else{
    							return true;
    						}
    					}
        			};
        			floor.addValidator(floorValid);
        			return floor;
        		}
        		//Setting Number of Elevators field:
        		else if (pid.equals("elevators")){
        			Select elevator = new Select("Number of Elevators:");
        			for(int i = 1; i<= 10; i++){
        				elevator.addItem(i);
        			}
        			elevator.setRequired(true);
        			elevator.setWidth("15em");
        			Validator elevValid = new Validator(){
    					public void validate(Object value)
    							throws InvalidValueException {
    						if(!isValid(value)){
    							throw new InvalidValueException("Please enter the number of elevators.");	
    						}
    					}
    					public boolean isValid(Object value) {
    						if(value.toString().equals("0")){
    							return false;
    						}
    						else{
    							return true;
    						}
    					}
        			};
        			elevator.addValidator(elevValid);
        			return elevator;
        		}
        		//Setting Scheduling Algorithm field:
        		else if (pid.equals("algorithm")){
        			Select algorithm = new Select("Scheduling Algorithm:");
        			algorithm.addItem("First Come First Serve");
        			algorithm.addItem("Shortest Distance First");
        			algorithm.setRequired(true);
        			algorithm.setRequiredError("Please select a scheduling algorithm.");
        			algorithm.addValidator(new StringLengthValidator("Please enter a scheduling algorithm", 1, 100, false));
        			algorithm.setWidth("20em");
        			return algorithm;
        		}
        		//If input does not match any of the specified fields:
        		return null;
        	}
        }
        
     
    public class MyFieldFactory2 implements FormFieldFactory{
    //OVERVIEW: MyFieldFactory2 is used to create, validate and define fields for the form 
    //          from the AddPassenger() method
        	
        public Field createField(Item item, Object propertyId, Component uiContext){
        //EFFECTS: fields for the form are displayed and inputs are validated	
        		
        	String pid = (String) propertyId;
        	if(pid.equals("time")){	
        	}else if (pid.equals("pfloor")){	
        	}else if (pid.equals("dfloor")){
        	}else if (pid.equals("constraint")){
        	}	
        	//If input does not match any of the specified fields:
        	return null;
        }
    }
    
    public class MyFieldFactory3 implements FormFieldFactory{
        //OVERVIEW: MyFieldFactory3 is used to create, validate and define fields for the form 
        //          from the StartSimulationView() method	
        	
        	public Field createField(Item item, Object propertyId, Component uiContext){
        	//EFFECTS: fields for the form are displayed and inputs are validated	
        		
        		String pid = (String) propertyId;
        		//Setting Simulation Name field:
        		if(pid.equals("name")){
        			TextField name = new TextField("Simulation Name:");
        			name.setRequired(true);
        			name.setRequiredError("Please enter a valid simulation name.");
        			name.setWidth("15em");
        			name.addValidator(new StringLengthValidator("Simulation name must be between 0 and 20 characters", 1, 22, false));
        			return name;
        		}
        		//Setting Simulation Template field:
        		else if (pid.equals("template")){
        			Select templates = new Select("Simulation Template:");
        			//GET THE TEMPLATES FROM THE DATABASE
        			for(int i = 0; i<5; i++){
        				templates.addItem("Template"+i);
        			}
        			templates.setRequired(true);
        			templates.setRequiredError("Please select a template for the simulation.");
        			templates.addValidator(new StringLengthValidator("Please select a template for the simulation", 1, 30, false));
        			templates.setWidth("30em");
        			return templates;
        		}
        		//If input does not match any of the specified fields:
        		return null;
        	}
        }

    public class MyFieldFactory4 implements FormFieldFactory{
        //OVERVIEW: MyFieldFactory4 is used to create, validate and define fields for the form 
        //          from the SimulationStopView() method	
        	
        public Field createField(Item item, Object propertyId, Component uiContext){
       	//EFFECTS: fields for the form are displayed and inputs are validated	
        		
       		String pid = (String) propertyId;
       		//Setting Scheduling Algorithm field:
       		if (pid.equals("algorithm")){
       			Select algorithm = new Select("Scheduling Algorithm:");
       			algorithm.addItem("First Come First Serve");
       			algorithm.addItem("Shortest Distance First");
       			algorithm.setWidth("20em");
       			return algorithm;
       		}//Setting Time in Service constraint:
        	else if(pid.equals("time")){
          		TextField time = new TextField("Take an elevator out of service when Time in Service exceeds the following amount of time quanta:");
       			time.setWidth("15em");
       			time.setNullRepresentation("");
       			time.addValidator(new IntegerValidator("Time in Service must be an integer no greater than 5000."){
       				@Override
       				protected boolean isValidString(String value){
       					int s = 0;
       					try{
       						s = Integer.parseInt(value);
       						if(s <= 5000){
       							return true;
       						}
       						else
       							return false;
       					}catch(Exception e){
       						return false;
       					}
       				}
       			});
           		Validator timeValid = new Validator(){
    				public void validate(Object value) throws InvalidValueException {
    					if(!isValid(value)){
    						throw new InvalidValueException("Time in Service cannot be negative.");	
    					}
    				}
    				public boolean isValid(Object value) {
    					String t = value.toString().substring(0, 1);
   						if(t.equals("-")|| value == null){
    						return false;
    					}else{
    						return true;
    					}
    				}
       			};
       			time.addValidator(timeValid);
       			return time;        			
       		}//Setting Distance Travelled constraint:
       		else if(pid.equals("distance")){
       			TextField distance = new TextField("Take an elevator out of service when Distance Travelled exceeds the following amount of floors:");
       			distance.setWidth("15em");
       			distance.setNullRepresentation("");
       			distance.addValidator(new IntegerValidator("Distance Travelled must be an integer no greater than 5000."){
       				@Override
       				protected boolean isValidString(String value){
       					int s = 0;
       					try{
       						s = Integer.parseInt(value);
       						if(s <= 5000){
       							return true;
       						}
       						else
       							return false;
       					}catch(Exception e){
       						return false;
       					}
       				}
       			});
           		Validator distValid = new Validator(){
    				public void validate(Object value) throws InvalidValueException {
    					if(!isValid(value)){
    						throw new InvalidValueException("Distance Travelled cannot be negative.");	
    					}
    				}
    				public boolean isValid(Object value) {
    					String t = value.toString().substring(0, 1);
   						if(t.equals("-")){
   							return false;
   						}
   						else{
   							return true;
    					}
    				}
        		};
        		distance.addValidator(distValid);
           		return distance;  
        	}//Setting Passenger limit:
       		else if(pid.equals("limit")){
       			TextField limit = new TextField("Set Passenger Limit per elevator:");
       			limit.setWidth("15em");
       			limit.setNullRepresentation("");
       			limit.addValidator(new IntegerValidator("Passenger Limit must be a positive integer no greater than 50."){
       				@Override
       				protected boolean isValidString(String value){
       					int s = 0;
       					try{
       						s = Integer.parseInt(value);
       						if(s <= 50){
       							return true;
       						}
       						else
       							return false;
       					}catch(Exception e){
       						return false;
       					}
       				}
       			});
       			Validator distValid = new Validator(){
    				public void validate(Object value) throws InvalidValueException {
    					if(!isValid(value)){
    						throw new InvalidValueException("Passenger Limit cannot be negative.");	
    					}
    				}
    				public boolean isValid(Object value) {
    					String t = value.toString().substring(0, 1);
   						if(t.equals("-")){
   							return false;
   						}
   						else{
   							return true;
    					}
    				}
        		};
        		limit.addValidator(distValid);
           		return limit;  
        	}
        	//If input does not match any of the specified fields:
        	return null;
       	}
    }
}

