import java.util.*;

public class Process {
    private static int numOfProcess = 0;
    private int num;
    private int burstTime;
    private int orgBurstTime;
    private int arrivalTime;
    private int priority;
    

    private List<Integer> startBurstTimeList = new ArrayList<>();
    private List<Integer> stopBurstTimeList = new ArrayList<>();
    private Map<Integer, Integer> burstTimeMap = new HashMap<>();
    private int queueTime;
    private int finishingTime;
    private int waitingTime;
    private int turnaroundTime;

    private boolean burstDone = false;

    public Process(int num, int burstTime, int arrivalTime, int priority) throws InputMismatchException{
        this.num = num;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        orgBurstTime = burstTime;
        this.priority = priority;
        if(burstTime < 0 || arrivalTime < 0 || priority < 0)
            throw new InputMismatchException("An positive integer is required");
        numOfProcess++;
    }

    public static int getNumOfProcess(){
        return numOfProcess;
    }

    public int getNum(){
        return num;
    }

    public int getBurstTime(){
        return burstTime;
    }

    public int getOrgBurstTime(){
        return orgBurstTime;
    }

    public int getArrivalTime(){
        return arrivalTime;
    }

    public int getPriority(){
        return priority;
    }

    public List<Integer> getStartBurstTimeList(){
        return startBurstTimeList;
    }
    public List<Integer> getStopBurstTimeList(){
        return stopBurstTimeList;
    }

    public Map<Integer, Integer> getBurstTimeMap(){
        return burstTimeMap;
    }

    public int getQueueTime(){
        return queueTime;
    }

    public int getFinishingTime(){
        return finishingTime;
    }
    
    public int getWaitingTime(){
        return waitingTime;
    }

    public int getTurnaroundTime(){
        return turnaroundTime;
    }

    public void setBurstTime(int burstTime){
        this.burstTime = burstTime;
    }

    public void setArrivalTime(int arrivalTime){
        this.arrivalTime = arrivalTime;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public void addStartBurstTime(int startBurstTime){
        this.startBurstTimeList.add(startBurstTime);
    }

    public void addStopBurstTime(int stopBurstTime){
        this.stopBurstTimeList.add(stopBurstTime);
    }

    public void setQueueTime(int queueTime){
        this.queueTime = queueTime;
    }

    public void setFinishingTime(int finishingTime){
        this.finishingTime = finishingTime;
    }

    public void setWaitingTime(int waitingTime){
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime){
        this.turnaroundTime = turnaroundTime;
    }

    public boolean hasDoneBurst(){
        return burstDone;
    }

    public void setBurstDone(boolean hasDoneBurst){
        burstDone = hasDoneBurst;
    }

    public void burst(int time){
        if(burstTimeMap.isEmpty())
            burstTimeMap.put(time, burstTime);
        if(burstTime > 0){
            burstTime--;
            burstTimeMap.put(time + 1, burstTime);
        }
    }

    public int getBurstTimeAtTime(int time){
        return burstTimeMap.get(time);
    }

    public String toString(){
        return "P" + num + ", BurstTime: " + orgBurstTime + ", ArrivalTime: " + arrivalTime + ", Priority: " + priority;
    }
}