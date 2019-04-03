/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnesstracker;

import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import javax.swing.JOptionPane;
/**
 *
 * @author 9632019
 */
public class FitnessTracker extends javax.swing.JFrame {

    /**
     * Creates new form FitnessTracker
     */
    public static ArrayList<Day> days = new ArrayList<Day>();
    public static int currentDay = -1;
    public static String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    public FitnessTracker() {
        initComponents();
        monthTable.getTableHeader().setReorderingAllowed(false);
        dayTotalTable.getTableHeader().setReorderingAllowed(false);
        dayGoalTable.getTableHeader().setReorderingAllowed(false);
    }
    public void load() {
        Calendar cal = Calendar.getInstance();
        String fileName = "data.txt";
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int numDays = Integer.parseInt(bufferedReader.readLine());
            for (int i = 0; i < numDays; i++) {
                int tempNum = Integer.parseInt(bufferedReader.readLine()); //stores day
                int tempNum2 = Integer.parseInt(bufferedReader.readLine()); //stores day of week
                days.add(new Day(tempNum, tempNum2, Integer.parseInt(bufferedReader.readLine())));
                currentDay++;
                days.get(currentDay).setBurnGoal(Integer.parseInt(bufferedReader.readLine()));
                int tempDuration = Integer.parseInt(bufferedReader.readLine());
                days.get(currentDay).addWorkout(tempDuration, Integer.parseInt(bufferedReader.readLine()));
                days.get(currentDay).setCalGoal(Integer.parseInt(bufferedReader.readLine()));
                days.get(currentDay).setCholesterolGoal(Integer.parseInt(bufferedReader.readLine()));
                days.get(currentDay).setFatGoal(Integer.parseInt(bufferedReader.readLine()));
                days.get(currentDay).setProteinGoal(Integer.parseInt(bufferedReader.readLine()));
                days.get(currentDay).setSodiumGoal(Integer.parseInt(bufferedReader.readLine()));
                days.get(currentDay).setSugarsGoal(Integer.parseInt(bufferedReader.readLine()));
                int numMeals = Integer.parseInt(bufferedReader.readLine());
                for (int z = 0; z < numMeals; z++) {
                    days.get(currentDay).addMeal();
                    days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).setTitle(bufferedReader.readLine());
                    days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).addItem(new Item(Integer.parseInt(bufferedReader.readLine()), Integer.parseInt(bufferedReader.readLine()), Integer.parseInt(bufferedReader.readLine()), Integer.parseInt(bufferedReader.readLine()), Integer.parseInt(bufferedReader.readLine()), Integer.parseInt(bufferedReader.readLine()), 1, "totalItem"));
                }
                //System.out.println("successfully loaded a day");
            }
            bufferedReader.close();
            //System.out.println("current day: " + days.get(currentDay).getNum());
            //System.out.println("actual day from calendar: " + cal.get(Calendar.DAY_OF_MONTH));
            if (days.get(currentDay).getNum() != cal.get(Calendar.DAY_OF_MONTH)) {
                days.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.WEEK_OF_MONTH)));
                currentDay++;
            }
            //System.out.println("updating gui to most recent day");
            dateDisplay.setText(dayNames[days.get(currentDay).getNumOfWeek() - 1] + ", " + monthNames[cal.get(Calendar.MONTH)] + " " + days.get(currentDay).getNum()); 
            monthlyTotalsLabel.setText("Monthly Calorie Totals / Goals - " + monthNames[cal.get(Calendar.MONTH)]); 
            if (days.get(currentDay).getCalGoal() != 0) {
                DefaultTableModel dayGoalModel = (DefaultTableModel)dayGoalTable.getModel();
                dayGoalModel.removeRow(0);
                dayGoalModel.addRow(new Object[]{days.get(currentDay).getCalGoal(), days.get(currentDay).getFatGoal(), days.get(currentDay).getSodiumGoal(), days.get(currentDay).getCholesterolGoal(), days.get(currentDay).getSugarsGoal(), days.get(currentDay).getProteinGoal(), days.get(currentDay).getBurnGoal()});
            }
            for (Day d : days) {
                DefaultTableModel monthCalModel = (DefaultTableModel)monthTable.getModel();
                monthCalModel.setValueAt(d.getTotalCal() +  " / " + d.getCalGoal(), (d.getWeekNum() - 1), d.getNumOfWeek() - 1);
            }
            if (!days.get(currentDay).meals.isEmpty()) {
                DefaultTableModel dayTotalModel = (DefaultTableModel)dayTotalTable.getModel();
                dayTotalModel.removeRow(0);
                dayTotalModel.addRow(new Object[]{days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getCal(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getFat(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getSodium(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getChol(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getSugars(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getProtein(), days.get(currentDay).getCalBurned()});      
                for (Meal m : days.get(currentDay).meals) {
                    DefaultTableModel dayMealModel = (DefaultTableModel)dayMealTable.getModel();
                    dayMealModel.addRow(new Object[]{m.getTitle(), m.getCal(), m.getFat(), m.getSodium(), m.getChol(), m.getSugars(), m.getProtein()});
                }
            }
        }
        catch(FileNotFoundException ex) {
            setup(); //run first time setup since either first-time user or save file was deleted            
        }
        catch(IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error reading save file.");        
        }
    }
    public void write() {
        String fileName = "data.txt";
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("" + days.size());
            bufferedWriter.newLine();
            for (Day wDay : days) {
                bufferedWriter.write("" + wDay.getNum());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getNumOfWeek());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getWeekNum());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getBurnGoal());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getDuration());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getCalBurned());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getCalGoal());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getCholesterolGoal());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getFatGoal());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getProteinGoal());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getSodiumGoal());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.getSugarsGoal());
                bufferedWriter.newLine();
                bufferedWriter.write("" + wDay.meals.size());
                bufferedWriter.newLine();
                if (!wDay.meals.isEmpty()) {
                    for (Meal wMeal : wDay.meals) {
                        bufferedWriter.write("" + wMeal.getTitle());
                        bufferedWriter.newLine();
                        bufferedWriter.write("" + wMeal.getCal());
                        bufferedWriter.newLine();
                        bufferedWriter.write("" + wMeal.getFat());
                        bufferedWriter.newLine();
                        bufferedWriter.write("" + wMeal.getSodium());
                        bufferedWriter.newLine();
                        bufferedWriter.write("" + wMeal.getChol());
                        bufferedWriter.newLine();
                        bufferedWriter.write("" + wMeal.getSugars());
                        bufferedWriter.newLine();
                        bufferedWriter.write("" + wMeal.getProtein());
                        bufferedWriter.newLine();
                        //System.out.println("just wrote a single meal");
                    }
                }
                //System.out.println("successfully wrote day to save file");
            }
            bufferedWriter.close();
        }
        catch(IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error writing to save file.");
        }
    }
    public void setup() {
        //System.out.println("first time setup");
        Calendar cal = Calendar.getInstance();
        days.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.WEEK_OF_MONTH)));
        currentDay++;
        dateDisplay.setText(dayNames[days.get(currentDay).getNumOfWeek() - 1] + ", " + monthNames[cal.get(Calendar.MONTH)] + " " + days.get(currentDay).getNum()); 
        //System.out.println("added new day" + days.get(currentDay).getNum() + "  " + days.get(currentDay).getNumOfWeek());
        DefaultTableModel monthCalModel = (DefaultTableModel)monthTable.getModel();
        monthCalModel.setValueAt(days.get(currentDay).getTotalCal() +  " / " + days.get(currentDay).getCalGoal(), (days.get(currentDay).getWeekNum() - 1), days.get(currentDay).getNumOfWeek() - 1);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mealWindow = new javax.swing.JDialog();
        addItem = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        MealTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        confirmMeal = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        mealName = new javax.swing.JTextField();
        itemWindow = new javax.swing.JDialog();
        itemNameLabel = new javax.swing.JLabel();
        calLabel = new javax.swing.JLabel();
        fatLabel = new javax.swing.JLabel();
        sodiumLabel = new javax.swing.JLabel();
        cholLabel = new javax.swing.JLabel();
        sugarsLabel = new javax.swing.JLabel();
        proteinLabel = new javax.swing.JLabel();
        servingsLabel = new javax.swing.JLabel();
        note = new javax.swing.JLabel();
        sugars = new javax.swing.JTextField();
        protein = new javax.swing.JTextField();
        chol = new javax.swing.JTextField();
        servings = new javax.swing.JTextField();
        fat = new javax.swing.JTextField();
        calories = new javax.swing.JTextField();
        title = new javax.swing.JTextField();
        sodium = new javax.swing.JTextField();
        confirm = new javax.swing.JButton();
        workoutWindow = new javax.swing.JDialog();
        calBurned = new javax.swing.JTextField();
        confirmButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        duration = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        monthTable = new javax.swing.JTable();
        monthlyTotalsLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        dayGoalTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        dayTotalTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        addMealButton = new javax.swing.JButton();
        addWorkoutButton = new javax.swing.JButton();
        updateGoals = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        dateDisplay = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        dayMealTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        mealWindow.setTitle("Add Meal");
        mealWindow.setBounds(new java.awt.Rectangle(0, 0, 600, 415));
        mealWindow.setLocation(new java.awt.Point(0, 0));
        mealWindow.setMinimumSize(null);
        mealWindow.setResizable(false);
        mealWindow.setSize(new java.awt.Dimension(600, 430));

        addItem.setText("Add Item");
        addItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemActionPerformed(evt);
            }
        });

        jLabel3.setText("Meal Totals");

        MealTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Calories", "Fat (g)", "Sodium (mg)", "Cholesterol (mg)", "Sugars (g)", "Protein (g)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        MealTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(MealTable);
        if (MealTable.getColumnModel().getColumnCount() > 0) {
            MealTable.getColumnModel().getColumn(0).setResizable(false);
            MealTable.getColumnModel().getColumn(1).setResizable(false);
            MealTable.getColumnModel().getColumn(2).setResizable(false);
            MealTable.getColumnModel().getColumn(3).setResizable(false);
            MealTable.getColumnModel().getColumn(4).setResizable(false);
            MealTable.getColumnModel().getColumn(5).setResizable(false);
            MealTable.getColumnModel().getColumn(6).setResizable(false);
        }

        jLabel4.setText("NOTE: All values shown are totaled for all servings.");

        confirmMeal.setText("Confirm");
        confirmMeal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmMealActionPerformed(evt);
            }
        });

        jLabel9.setText("Meal Name:");

        javax.swing.GroupLayout mealWindowLayout = new javax.swing.GroupLayout(mealWindow.getContentPane());
        mealWindow.getContentPane().setLayout(mealWindowLayout);
        mealWindowLayout.setHorizontalGroup(
            mealWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mealWindowLayout.createSequentialGroup()
                .addGroup(mealWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mealWindowLayout.createSequentialGroup()
                        .addGroup(mealWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mealWindowLayout.createSequentialGroup()
                                .addGap(255, 255, 255)
                                .addComponent(jLabel3))
                            .addGroup(mealWindowLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(addItem)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(mealWindowLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mealWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mealWindowLayout.createSequentialGroup()
                                .addComponent(confirmMeal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(mealWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mealWindowLayout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mealName, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel4))))))
                .addContainerGap())
        );
        mealWindowLayout.setVerticalGroup(
            mealWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mealWindowLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mealWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addItem)
                    .addGroup(mealWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(mealName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mealWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmMeal)
                    .addComponent(jLabel4))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        itemWindow.setTitle("Add Item");
        itemWindow.setBounds(new java.awt.Rectangle(0, 0, 400, 390));
        itemWindow.setMinimumSize(null);
        itemWindow.setResizable(false);
        itemWindow.setSize(new java.awt.Dimension(390, 400));

        itemNameLabel.setText("Item Name:");

        calLabel.setText("Calories:");

        fatLabel.setText("Fat (g):");

        sodiumLabel.setText("Sodium (mg):");

        cholLabel.setText("Cholesterol (mg):");

        sugarsLabel.setText("Sugars (g):");

        proteinLabel.setText("Protein (g):");

        servingsLabel.setText("# Servings:");

        note.setText("NOTE: All nutritional values should be per serving.");

        sugars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sugarsActionPerformed(evt);
            }
        });

        protein.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proteinActionPerformed(evt);
            }
        });

        chol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cholActionPerformed(evt);
            }
        });

        servings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servingsActionPerformed(evt);
            }
        });

        fat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fatActionPerformed(evt);
            }
        });

        calories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                caloriesActionPerformed(evt);
            }
        });

        title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleActionPerformed(evt);
            }
        });

        sodium.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sodiumActionPerformed(evt);
            }
        });

        confirm.setText("Confirm");
        confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout itemWindowLayout = new javax.swing.GroupLayout(itemWindow.getContentPane());
        itemWindow.getContentPane().setLayout(itemWindowLayout);
        itemWindowLayout.setHorizontalGroup(
            itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemNameLabel)
                    .addGroup(itemWindowLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(itemWindowLayout.createSequentialGroup()
                                .addComponent(note)
                                .addGap(18, 18, 18)
                                .addComponent(confirm))
                            .addGroup(itemWindowLayout.createSequentialGroup()
                                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cholLabel)
                                    .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(proteinLabel)
                                        .addComponent(sugarsLabel))
                                    .addComponent(servingsLabel)
                                    .addComponent(sodiumLabel)
                                    .addComponent(fatLabel)
                                    .addComponent(calLabel))
                                .addGap(18, 18, 18)
                                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(calories, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fat, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(servings, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sugars, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(protein, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(chol, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sodium, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        itemWindowLayout.setVerticalGroup(
            itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemNameLabel)
                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calLabel)
                    .addComponent(calories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fatLabel)
                    .addComponent(fat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sodiumLabel)
                    .addComponent(sodium, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cholLabel)
                    .addComponent(chol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sugarsLabel)
                    .addComponent(sugars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proteinLabel)
                    .addComponent(protein, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(servingsLabel)
                    .addComponent(servings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(itemWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(note)
                    .addComponent(confirm))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        workoutWindow.setTitle("Add Workout");
        workoutWindow.setMinimumSize(null);
        workoutWindow.setResizable(false);
        workoutWindow.setSize(new java.awt.Dimension(270, 105));

        calBurned.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calBurnedActionPerformed(evt);
            }
        });

        confirmButton.setText("Confirm");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        jLabel7.setText("Calories Burned:");

        jLabel8.setText("Duration (hrs):");

        duration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                durationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout workoutWindowLayout = new javax.swing.GroupLayout(workoutWindow.getContentPane());
        workoutWindow.getContentPane().setLayout(workoutWindowLayout);
        workoutWindowLayout.setHorizontalGroup(
            workoutWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workoutWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(workoutWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, workoutWindowLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calBurned, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(confirmButton))
                    .addGroup(workoutWindowLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(duration, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        workoutWindowLayout.setVerticalGroup(
            workoutWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workoutWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(workoutWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmButton)
                    .addComponent(jLabel7)
                    .addComponent(calBurned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(workoutWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(duration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Fitness Tracker");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        monthTable.setAutoCreateRowSorter(true);
        monthTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Sun", "Mon", "Tue", "Wed", "Thurs", "Fri", "Sat"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        monthTable.setFillsViewportHeight(true);
        monthTable.setMinimumSize(new java.awt.Dimension(105, 78));
        monthTable.setOpaque(false);
        monthTable.setPreferredSize(new java.awt.Dimension(525, 77));
        monthTable.setRowHeight(17);
        jScrollPane2.setViewportView(monthTable);
        if (monthTable.getColumnModel().getColumnCount() > 0) {
            monthTable.getColumnModel().getColumn(0).setResizable(false);
            monthTable.getColumnModel().getColumn(1).setResizable(false);
            monthTable.getColumnModel().getColumn(2).setResizable(false);
            monthTable.getColumnModel().getColumn(3).setResizable(false);
            monthTable.getColumnModel().getColumn(4).setResizable(false);
            monthTable.getColumnModel().getColumn(5).setResizable(false);
            monthTable.getColumnModel().getColumn(6).setResizable(false);
        }

        monthlyTotalsLabel.setText("Monthly Calorie Totals / Goals");

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        dayGoalTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Calories (consumed)", "Fat (g)", "Sodium (mg)", "Cholesterol (mg)", "Sugars (g)", "Protein (g)", "Calories (burned)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        dayGoalTable.setRowHeight(32);
        dayGoalTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(dayGoalTable);
        if (dayGoalTable.getColumnModel().getColumnCount() > 0) {
            dayGoalTable.getColumnModel().getColumn(0).setResizable(false);
            dayGoalTable.getColumnModel().getColumn(1).setResizable(false);
            dayGoalTable.getColumnModel().getColumn(2).setResizable(false);
            dayGoalTable.getColumnModel().getColumn(3).setResizable(false);
            dayGoalTable.getColumnModel().getColumn(4).setResizable(false);
            dayGoalTable.getColumnModel().getColumn(5).setResizable(false);
            dayGoalTable.getColumnModel().getColumn(6).setResizable(false);
        }

        jLabel5.setText("Daily Totals");

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        dayTotalTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Calories (consumed)", "Fat (g)", "Sodium (mg)", "Cholesterol (mg)", "Sugars (g)", "Protein (g)", "Calories (burned)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dayTotalTable.setRowHeight(32);
        dayTotalTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(dayTotalTable);
        if (dayTotalTable.getColumnModel().getColumnCount() > 0) {
            dayTotalTable.getColumnModel().getColumn(0).setResizable(false);
            dayTotalTable.getColumnModel().getColumn(1).setResizable(false);
            dayTotalTable.getColumnModel().getColumn(2).setResizable(false);
            dayTotalTable.getColumnModel().getColumn(3).setResizable(false);
            dayTotalTable.getColumnModel().getColumn(4).setResizable(false);
            dayTotalTable.getColumnModel().getColumn(5).setResizable(false);
            dayTotalTable.getColumnModel().getColumn(6).setResizable(false);
        }

        jLabel6.setText("Daily Goals (editable)");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        addMealButton.setText("ADD MEAL");
        addMealButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMealButtonActionPerformed(evt);
            }
        });

        addWorkoutButton.setText("ADD WORKOUT");
        addWorkoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWorkoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addWorkoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addMealButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(addMealButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addWorkoutButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        updateGoals.setText("UPDATE GOALS");
        updateGoals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateGoalsActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("DATE:");

        dateDisplay.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        dateDisplay.setText("(date)");

        resetButton.setText("RESET");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        dayMealTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Calories (consumed)", "Fat (g)", "Sodium (mg)", "Cholesterol (mg)", "Sugars (g)", "Protein (g)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dayMealTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(dayMealTable);
        if (dayMealTable.getColumnModel().getColumnCount() > 0) {
            dayMealTable.getColumnModel().getColumn(0).setResizable(false);
            dayMealTable.getColumnModel().getColumn(1).setResizable(false);
            dayMealTable.getColumnModel().getColumn(2).setResizable(false);
            dayMealTable.getColumnModel().getColumn(3).setResizable(false);
            dayMealTable.getColumnModel().getColumn(4).setResizable(false);
            dayMealTable.getColumnModel().getColumn(5).setResizable(false);
            dayMealTable.getColumnModel().getColumn(6).setResizable(false);
        }

        jLabel2.setText("Today's Meals");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .addComponent(jLabel1)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(monthlyTotalsLabel)
                                .addGap(328, 328, 328)
                                .addComponent(resetButton))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(updateGoals))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(monthlyTotalsLabel)
                            .addComponent(resetButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(updateGoals)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addMealButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMealButtonActionPerformed
        if (days.size() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Please add a Day before proceeding.");
        }
        else {
            days.get(currentDay).addMeal();
            mealWindow.setVisible(true);
        }
    }//GEN-LAST:event_addMealButtonActionPerformed

    private void addItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemActionPerformed
        itemWindow.setVisible(true);
    }//GEN-LAST:event_addItemActionPerformed

    private void sugarsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sugarsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sugarsActionPerformed

    private void proteinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proteinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_proteinActionPerformed

    private void cholActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cholActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cholActionPerformed

    private void servingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servingsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_servingsActionPerformed

    private void fatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fatActionPerformed

    private void caloriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_caloriesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_caloriesActionPerformed

    private void titleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titleActionPerformed

    private void sodiumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sodiumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sodiumActionPerformed

    private void confirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmActionPerformed
        DefaultTableModel mealModel = (DefaultTableModel)MealTable.getModel();
        boolean success = false;
        try {
            days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).addItem(new Item(Integer.parseInt(calories.getText()), Integer.parseInt(fat.getText()), Integer.parseInt(sodium.getText()), Integer.parseInt(chol.getText()), Integer.parseInt(sugars.getText()), Integer.parseInt(protein.getText()), Integer.parseInt(servings.getText()), title.getText()));
            success = true;
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(rootPane, "Please make sure all nutritional values are numbers before proceeding.");
        }
        if (success) {
            //System.out.println("added item into the meal");
            itemWindow.dispose();
            //System.out.println("updating table contents");
            mealModel.addRow(new Object[]{days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.get(days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.size() - 1).getTitle(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.get(days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.size() - 1).getCal(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.get(days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.size() - 1).getFat(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.get(days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.size() - 1).getSodium(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.get(days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.size() - 1).getChol(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.get(days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.size() - 1).getSugars(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.get(days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).items.size() - 1).getProtein()});
            title.setText(null);
            calories.setText(null);
            fat.setText(null);
            sodium.setText(null);
            chol.setText(null);
            sugars.setText(null);
            protein.setText(null);
            servings.setText(null);
        }
    }//GEN-LAST:event_confirmActionPerformed

    private void addWorkoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWorkoutButtonActionPerformed
        if (days.size() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Please add a Day before proceeding.");
        }
        else {
            workoutWindow.setVisible(true);
        }
    }//GEN-LAST:event_addWorkoutButtonActionPerformed

    private void calBurnedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calBurnedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_calBurnedActionPerformed

    private void durationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_durationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_durationActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        boolean success = false;
        try {
            days.get(currentDay).addWorkout(Integer.parseInt(duration.getText()), Integer.parseInt(calBurned.getText()));
            success = true;
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(rootPane, "Please make sure all values are numbers before proceeding.");
        }
        if (success) {
            //System.out.println("added workout to day");
            workoutWindow.dispose();
            calBurned.setText(null);
            duration.setText(null);
            DefaultTableModel dayTotalModel = (DefaultTableModel)dayTotalTable.getModel();
            dayTotalModel.removeRow(0);
            dayTotalModel.addRow(new Object[]{days.get(currentDay).getTotalCal(), days.get(currentDay).getTotalFat(), days.get(currentDay).getTotalSodium(), days.get(currentDay).getTotalCholesterol(), days.get(currentDay).getTotalSugars(), days.get(currentDay).getTotalProtein(), days.get(currentDay).getCalBurned()});     
            DefaultTableModel monthCalModel = (DefaultTableModel)monthTable.getModel();
            monthCalModel.setValueAt(days.get(currentDay).getTotalCal() +  " / " + days.get(currentDay).getCalGoal(), (days.get(currentDay).getWeekNum() - 1), days.get(currentDay).getNumOfWeek() - 1);
        }
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void confirmMealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmMealActionPerformed
        days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).setTitle(mealName.getText());
        //System.out.println("meal successfully added to the day");
        mealWindow.dispose();
        DefaultTableModel mealModel = (DefaultTableModel)MealTable.getModel();
        mealModel.setRowCount(0);
        mealName.setText(null);
        DefaultTableModel dayTotalModel = (DefaultTableModel)dayTotalTable.getModel();
        dayTotalModel.removeRow(0);
        dayTotalModel.addRow(new Object[]{days.get(currentDay).getTotalCal(), days.get(currentDay).getTotalFat(), days.get(currentDay).getTotalSodium(), days.get(currentDay).getTotalCholesterol(), days.get(currentDay).getTotalSugars(), days.get(currentDay).getTotalProtein(), days.get(currentDay).getCalBurned()});     
        DefaultTableModel monthCalModel = (DefaultTableModel)monthTable.getModel();
        monthCalModel.setValueAt(days.get(currentDay).getTotalCal() +  " / " + days.get(currentDay).getCalGoal(), (days.get(currentDay).getWeekNum() - 1), days.get(currentDay).getNumOfWeek() - 1);
        DefaultTableModel dayMealModel = (DefaultTableModel)dayMealTable.getModel();
        dayMealModel.addRow(new Object[]{days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getTitle(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getCal(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getFat(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getSodium(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getChol(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getSugars(), days.get(currentDay).meals.get(days.get(currentDay).meals.size() - 1).getProtein()});
    }//GEN-LAST:event_confirmMealActionPerformed

    private void updateGoalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateGoalsActionPerformed
        if (days.size() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Please add a Day before proceeding.");
        }
        else {
            DefaultTableModel dayGoalModel = (DefaultTableModel)dayGoalTable.getModel();
            boolean success = false;
            try {
                days.get(currentDay).setCalGoal((int)dayGoalTable.getValueAt(0, 0));
                days.get(currentDay).setFatGoal((int)dayGoalTable.getValueAt(0, 1));
                days.get(currentDay).setSodiumGoal((int)dayGoalTable.getValueAt(0, 2));
                days.get(currentDay).setCholesterolGoal((int)dayGoalTable.getValueAt(0, 3));
                days.get(currentDay).setSugarsGoal((int)dayGoalTable.getValueAt(0, 4));
                days.get(currentDay).setProteinGoal((int)dayGoalTable.getValueAt(0, 5));
                days.get(currentDay).setBurnGoal((int)dayGoalTable.getValueAt(0, 6));
                success = true;
            }
            catch (NullPointerException e) {
                JOptionPane.showMessageDialog(rootPane, "Please make sure all goal values are filled out and confirmed before updating.");
            }
            if (success) {
                DefaultTableModel monthCalModel = (DefaultTableModel)monthTable.getModel();
                monthCalModel.setValueAt(days.get(currentDay).getTotalCal() +  " / " + days.get(currentDay).getCalGoal(), (days.get(currentDay).getWeekNum() - 1), days.get(currentDay).getNumOfWeek() - 1);
                JOptionPane.showMessageDialog(rootPane, "Daily goals successfully updated.");
                //System.out.println("daily goals updated");
            }
        }
    }//GEN-LAST:event_updateGoalsActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        write();
    }//GEN-LAST:event_formWindowClosing

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset the entire month?  Program will close.", "Confirm", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            String fileName = "data.txt";
            File f = new File(fileName);
            f.delete();
            days.clear();
            DefaultTableModel monthCalModel = (DefaultTableModel)monthTable.getModel();
            for (int i = 0; i < 5; i++) {
                monthCalModel.removeRow(0);
            }
            for (int i = 0; i < 5; i++) {
                monthCalModel.addRow(new Object[]{null, null, null, null, null, null, null});
            }
            System.exit(0);  
        }
    }//GEN-LAST:event_resetButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FitnessTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FitnessTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FitnessTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FitnessTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FitnessTracker fit = new FitnessTracker();
                fit.setVisible(true);
                fit.load();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable MealTable;
    private javax.swing.JButton addItem;
    private javax.swing.JButton addMealButton;
    private javax.swing.JButton addWorkoutButton;
    private javax.swing.JTextField calBurned;
    private javax.swing.JLabel calLabel;
    private javax.swing.JTextField calories;
    private javax.swing.JTextField chol;
    private javax.swing.JLabel cholLabel;
    private javax.swing.JButton confirm;
    private javax.swing.JButton confirmButton;
    private javax.swing.JButton confirmMeal;
    private javax.swing.JLabel dateDisplay;
    private javax.swing.JTable dayGoalTable;
    private javax.swing.JTable dayMealTable;
    private javax.swing.JTable dayTotalTable;
    private javax.swing.JTextField duration;
    private javax.swing.JTextField fat;
    private javax.swing.JLabel fatLabel;
    private javax.swing.JLabel itemNameLabel;
    private javax.swing.JDialog itemWindow;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextField mealName;
    private javax.swing.JDialog mealWindow;
    private javax.swing.JTable monthTable;
    private javax.swing.JLabel monthlyTotalsLabel;
    private javax.swing.JLabel note;
    private javax.swing.JTextField protein;
    private javax.swing.JLabel proteinLabel;
    private javax.swing.JButton resetButton;
    private javax.swing.JTextField servings;
    private javax.swing.JLabel servingsLabel;
    private javax.swing.JTextField sodium;
    private javax.swing.JLabel sodiumLabel;
    private javax.swing.JTextField sugars;
    private javax.swing.JLabel sugarsLabel;
    private javax.swing.JTextField title;
    private javax.swing.JButton updateGoals;
    private javax.swing.JDialog workoutWindow;
    // End of variables declaration//GEN-END:variables
}
