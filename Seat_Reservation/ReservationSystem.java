import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
//We have reservation system which consist of the seat grid and 2 inner classes: User and Seat
//User is runnable and will run in a separate thread.
//Main takes the inputs and creates a reservations system instance , creates all players along with the seat grid for the ReservationSystem instance.
public class ReservationSystem {
    //we will have user and seat as inner classes each seat has reserve method which is synchronized
    private  static  ArrayList<ArrayList<Seat>> seatGrid;
    public ArrayList<ArrayList<Seat>> get_seatgrid(){
        return seatGrid;
    }
    public ReservationSystem(int N , int M){
        seatGrid=new ArrayList<>();
        for(int i=0; i<N ; i++){
            seatGrid.add(new ArrayList<Seat>());
        }
    }
    public void arrangeSeats(ArrayList<Seat> seats){
        Comparator<Seat> comparator = new Comparator<Seat>(){
            @Override
            public int compare(Seat a , Seat b)
            {
                return a.get_name().compareTo(b.get_name());
            }
        };
        seats.sort(comparator);
    }
    public void Print_Status(){// Printing the grid status for the current Session
        for(int i=0; i<seatGrid.size();i++){
            String res="";
            for(int j=0;j< seatGrid.get(i).size();j++){
                if(seatGrid.get(i).get(j).is_reserved){
                    res+="T:"+seatGrid.get(i).get(j).get_holder_name()+" ";
                }
                else{
                    res+="E: ";
                }
            }
            System.out.println(res);
        }
    }
    public void Add_Seat(String name,int index){
        seatGrid.get(index).add(new Seat(name));
    }
    public class Seat {
        private String name;
        private  String holder_name;
        private  boolean is_reserved;
        private  Lock lock = new ReentrantLock();// will be used to syncronize check and reserve methods
        public String get_name(){return name;}
        public String get_holder_name(){return holder_name;}
        public Seat(String Name){
            is_reserved=false;
            name=Name;
        }
        public void give_up(User loser){// If holder has this seat it will give it up.
            lock.lock();
            if(holder_name==loser.get_name()){
                holder_name="";
                is_reserved=false;
            }
            lock.unlock();
        }
        public boolean reserve(User holder){// reserves the seat for holder if it is not holded by another user

            lock.lock();
            if(is_reserved){
                lock.unlock();
                return false;
            }
            try {
                holder_name=holder.get_name();
                is_reserved=true;
            }
            finally {
                lock.unlock();
                return true;
            }
        }
        public boolean check_reserved(){//check if it is reserved by another user
            boolean result=false;
            lock.lock(); // this will prevent checking the seat's state when it is getting reserved
            try {
                result=is_reserved;
            }
            finally {
                lock.unlock();
                return result;
            }
        }
    }
    public class User implements  Runnable{
        private String name;
        private  ArrayList<Seat> wanted_seats;
        public String get_name(){
            return name;
        }
        public User(String Name , ArrayList<Seat> seats){
            name=Name;
            wanted_seats=seats;
        }
        public String Get_wantedSeats(){ // returns wanted_seat names as a String format of [Seat1,Seat2,...]
            String res="[";
            for(int i=0; i<wanted_seats.size();i++){
                if(i==wanted_seats.size()-1){
                    res+=wanted_seats.get(i).get_name();
                    continue;
                }
                res+=wanted_seats.get(i).get_name()+", ";

            }
            res+="]";
            return res;
        }
        public void run(){// user routine
            boolean shouldRetry=true;
            while(shouldRetry){
                int counter=0;
                for(int i=0; i<wanted_seats.size();i++){// check if all the wanted seats are available?
                    if(!wanted_seats.get(i).check_reserved()){
                        counter++;
                    }
                }
                if(counter == wanted_seats.size()) {// all seats available.
                        double database_state=Math.random();
                        if(database_state>0.1){// data base didn't fail
                            int give_up_flag=0;
                            for(int h=0;h<wanted_seats.size();h++){
                                boolean succeed=wanted_seats.get(h).reserve(this);
                                if(!succeed){// already reserved by another user deadlock case
                                    //give up all the seats already taken
                                    give_up_flag=1;
                                    for(int g=0; g<h; g++){
                                        wanted_seats.get(g).give_up(this);
                                    }
                                    Logger.LogFailedReservation(this.get_name(),this.Get_wantedSeats(),System.nanoTime(),"Some of the wanted seats are already reserved before I came , I am giving up");
                                    shouldRetry=false;
                                    break;
                                }
                            }
                            if(give_up_flag==0){
                                try {//Sleep 100ms
                                    Thread.sleep(50);
                                    Logger.LogSuccessfulReservation(this.get_name(),this.Get_wantedSeats(),System.nanoTime(),"Succesfully reserved all the seats");
                                    shouldRetry=false;
                                }
                                catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        }
                        else{// data base failed.
                            Logger.LogDatabaseFailiure(this.get_name(),this.Get_wantedSeats(),System.nanoTime(),"Database failed sleep for 100ms");
                            try {//Sleep 100ms
                                Thread.sleep(100);
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                }
                else{// some of the seats are already taken.
                    Logger.LogFailedReservation(this.get_name(),this.Get_wantedSeats(),System.nanoTime(),"Reservation failed not all wanted seats are empty"); // logger failed
                    shouldRetry=false;
                }

            }
        }
    }
    public User Create_User(String Name,ArrayList<Seat> seats ){
        return new User(Name,seats);
    }

}
