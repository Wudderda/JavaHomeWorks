
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) { // Take the input create the seats and users start threads with executers.
        ExecutorService executor = Executors.newCachedThreadPool();
        ArrayList<ReservationSystem.User> People=new ArrayList<ReservationSystem.User>();


        Scanner scan= new Scanner(System.in);
        String first_line=scan.nextLine();
        String[] NXM =first_line.split(" ");
        int N= Integer.parseInt(NXM[0]);
        int M= Integer.parseInt(NXM[1]);
        String second_line=scan.nextLine();
        ReservationSystem Session=new ReservationSystem(N,M);
        //seats creation for seat grid
        for(int a=0;a<N ;a++){
            for(int b=0 ; b< M ;b++){
                char prefix=((char)(65+a)); // Creation of A-Z chars
                char posix=((char)(48+b)); // 1-9 Chars
                String name= ""+prefix+posix;//Seat names
                Session.Add_Seat(name,a);//creating the seats
            }
        }
        int K= Integer.parseInt(second_line);//number of users


        for(int i =0; i< K ; i++){// create players with their wanted seats array
            String nth_line=scan.nextLine();
            String[] Line_info=nth_line.split(" ");//Line_info[0] is player's name and the rest is the wanted seats of him
            ArrayList<ReservationSystem.Seat> Player_seats=new ArrayList<ReservationSystem.Seat>();
            for(int j=1;j<Line_info.length;j++){
                char seat_prefix= Line_info[j].charAt(0);
                int index1=((int) seat_prefix)-65;// first or sec index
                char seat_posix= Line_info[j].charAt(1);
                int index2= ((int)seat_posix)-48;// other index
                Player_seats.add(Session.get_seatgrid().get(index1).get(index2));// get the seat by indexes and add it to the Player's wanted_seat list
            }
            Session.arrangeSeats(Player_seats);// this is to prevent deadlock . We have a common order among seat reservations.Whoever comes first gets the seat.
            People.add(Session.Create_User(Line_info[0],Player_seats)); //All Users will be stored in this array to be executed in separate thread later.

        }
        Logger.InitLogger();
        for(int h=0; h<People.size(); h++){
            executor.execute(People.get(h));
        }
        executor.shutdown();
        while(!executor.isTerminated()){// wait until all processes are finished
        }
        Session.Print_Status(); //When it is all over , print the status of the grid
    }
}
