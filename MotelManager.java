package motelManager;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Scanner;
import javax.swing.*;

class Room{
    private int guests;
    private int roomNo;
    private double rate;
    private boolean smoking;
    static int EmptyRooms;
    static JLabel data[] = new JLabel[10*4];
    static JLabel emptyRoomLabel;
    static NumberFormat currency;
    static{
        EmptyRooms = 0;
        currency = NumberFormat.getCurrencyInstance();
    }
    void setRoomDetails(Scanner DiskScanner){
        this.guests = DiskScanner.nextInt();
        this.rate = DiskScanner.nextDouble();
        this.smoking = DiskScanner.nextBoolean();

        if(this.guests == 0)
            EmptyRooms++;
    }
//FOR RUNNING CODE WITHOUT GUI
/*    public void getRoomDetails(){
 *        System.out.print("\t" + roomNo);
 *        System.out.print("\t\t" + guests);
 *        System.out.print("\t\t" + currency.format(rate));
 *        System.out.println("\t\t\t\t" + (smoking ? "yes" : "no"));
 *     }
 **/

    int getGuests() {
        return guests;
    }

    void setGuests(int guests) {
        this.guests = guests;
    }

    double getRate() {
        return rate;
    }

    void setRate(double rate) {
        this.rate = rate;
    }

    boolean getSmoking() {
        return smoking;
    }

    void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    int getRoomNo() {
        return roomNo;
    }

    void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    void MakeRoomEmpty() {
        //setting everything with default values.
        this.guests = 0;
        this.smoking = false;
        this.rate = 60.0;
        EmptyRooms++;
    }
}
@SuppressWarnings("serial")
class FrameGenerator extends JPanel {
    FrameGenerator(Room[] rooms){
        setLayout(new GridLayout(10+1+1,4,20,3));

        add(new JLabel("Room No"));add(new JLabel("Guests"));
        add(new JLabel("Rate/hour"));add(new JLabel("Smoking ?"));

        int i = 0;
        for(Room room : rooms){
            add(Room.data[i*4+0] = new JLabel(" " + String.valueOf(room.getRoomNo())));
            add(Room.data[i*4+1] = new JLabel(String.valueOf(room.getGuests())));
            add(Room.data[i*4+2] = new JLabel(Room.currency.format(room.getRate())));
            add(Room.data[i*4+3] = new JLabel(String.valueOf(room.getSmoking())));
            i++;
        }

        JButton add_guest = new JButton("Add Guest");
        JButton remove_guest = new JButton("Remove Guest");
        JButton save = new JButton("Save");

        add(add_guest);
        add(remove_guest);
        add(save);
        add(Room.emptyRoomLabel = new JLabel("Empty Rooms : " + Room.EmptyRooms));
        add_guest.addActionListener(new Add_Guest());
        remove_guest.addActionListener(new Remove_Guest());
        save.addActionListener(new Save_List());
        setVisible(true);
    }

    private class Add_Guest implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MotelManager.AddNewGuest();
        }

    }


    private class Remove_Guest implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MotelManager.RemoveGuest();
        }
    }

    private class Save_List implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try {
                MotelManager.StoreDetails();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
public class MotelManager{
    static Room[] rooms = new Room[10];
    public static FrameGenerator content;
    public static void main(String[] args) throws IOException{
        //Getting Data From File to rooms
        Scanner DiskScanner = new Scanner(new File("D:\\My CODEs\\Java Projects\\GUI_BASED\\src\\motelManager\\Motel.txt"));
        for(int i = 0 ;DiskScanner.hasNext();i++){
            rooms[i] = new Room();//Must Remember to assign memory to address
            rooms[i].setRoomDetails(DiskScanner);
            rooms[i].setRoomNo(i+1);
        }
        content = new FrameGenerator(rooms);
        JFrame motelManager = new JFrame("Motel Manager");
        motelManager.setVisible(true);
        motelManager.setLocationRelativeTo(null);
        motelManager.add(content);
        motelManager.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        motelManager.pack();
    }

     static void StoreDetails() throws  IOException{
        PrintStream printStream = new PrintStream(new File("D:\\My CODEs\\Java Projects\\GUI_BASED\\src\\motelManager\\Motel.txt"));
        for(Room room : rooms){
            printStream.println(room.getGuests());
            printStream.println(room.getRate());
            printStream.println(room.getSmoking());
        }
    }

     static void RemoveGuest() {
        if(Room.EmptyRooms == 10) {
            JOptionPane.showMessageDialog(null,"All rooms are empty. ");
            return;
        }

        int roomNo = Integer.parseInt(JOptionPane.showInputDialog("Enter Room No of Guest to SayGoodBye"));

        if(roomNo>10){
            JOptionPane.showMessageDialog(null,"Room No Is Not Valid");
            return;
        }
        if(rooms[roomNo-1].getGuests() == 0){
            JOptionPane.showMessageDialog(null,"Room is already empty...!");
            return;
        }
        int index = 0;
        for(Room room : rooms){
            if(room.getRoomNo()==roomNo){
             room.MakeRoomEmpty();
             break;
            }
            index++;
        }
        PrintDetails(index);
    }

    private static void PrintDetails(int index){
            Room.emptyRoomLabel.setText("Empty Rooms : "+Room.EmptyRooms);
            Room.data[index*4+0].setText(" " + String.valueOf(rooms[index].getRoomNo()));
            Room.data[index*4+1].setText(String.valueOf(rooms[index].getGuests()));
            Room.data[index*4+2].setText(Room.currency.format(rooms[index].getRate()));
            Room.data[index*4+3].setText(String.valueOf(rooms[index].getSmoking()));
    }

    static void AddNewGuest() {
        //Function to add new Guest if Room is Empty
        int i = 0;
        if(Room.EmptyRooms!=0)
            for( ; i < rooms.length ; i++){
                if(rooms[i].getGuests()==0){
                    rooms[i].setGuests(Integer.parseInt(JOptionPane.showInputDialog("No OF Guests?")));
                    rooms[i].setRate(Double.parseDouble(JOptionPane.showInputDialog("Rate ? (default : 60.0)")));
                    rooms[i].setSmoking(Boolean.parseBoolean(JOptionPane.showInputDialog("Smoking? ( true/false )")));
                    Room.EmptyRooms--;
                    break;
                }
            }
        else
            JOptionPane.showMessageDialog(null,"NO EMPTY ROOMS AVAILABLE");
        PrintDetails(i);
    }
}
//DATA INSIDE THE FILE
/*
* 1
60.0
true
2
70.0
true
0
60.0
false
4
80.0
false
5
90.0
true
0
60.0
true
2
70.0
false
1
60.0
false
2
70.0
true
4
80.0
true

* */
//FOR GETTING INPUT FROM USER
/*
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\TANAY\\IdeaProjects\\test\\GUI_On_Test\\src\\Motel.txt");
        int i;
        String string = "";
        System.out.println("Get String");
        while((i=fileInputStream.read())!=-1)
        {
            System.out.print((char)i);
            string+=(char)i;
        }
*/
