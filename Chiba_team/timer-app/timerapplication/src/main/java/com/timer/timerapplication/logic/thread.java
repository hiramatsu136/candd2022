public class thread {
    public static void main(String[] args){
        Alarm_thread alarmthread = new Alarm_thread();
        Thread alarm = new Thread(alarmthread);
        alarm.start();

        alarm.interrupt();
    } 
}

class Alarm_thread implements Runnable {
    @Override

    public void run(){
        try{
            //アラーム待機処理
            wait(1);
        }catch(InterruptedException e){
            System.out.println("アラームを停止します。");
            e.printStackTrace();
            return;
        }
    }
}