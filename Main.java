import java.util.*;

public class Main {

    public Main(CpuScheduling c){
        int time = 0;
        boolean processing = true;

        int processNum = 0;
        int burstTime = 0;
        int arrTime = 0;
        int priority = 0;
        int inputMode = 0;
        
        Scanner input = new Scanner(System.in);
        
        System.out.println("What input data mode you want?");
        System.out.println("1. User Input Data");
        System.out.println("2. Preset Data");
        do{
            inputMode = input.nextInt();
            if(inputMode == 1){
                System.out.println("Type like this: ");
                System.out.println("Process | BurstTime  ArrivalTime  Priority");
                System.out.println("Example:  P0  | 8 0 2");
                System.out.println("*Type -1 for burst time, arrival time, and priority to stop inputing*");           
                System.out.print("   P" + processNum + "   |");
                burstTime = input.nextInt();
                arrTime = input.nextInt();
                priority = input.nextInt();
            
                do{
                    try{
                        c.addToProcessList(new Process(processNum, burstTime, arrTime, priority));
                        processNum++;
                    }
                    catch(InputMismatchException ex){
                        System.out.println(ex.getMessage());
                    }
                    System.out.print("   P" + processNum + "   |");
                    burstTime = input.nextInt();
                    arrTime = input.nextInt();
                    priority = input.nextInt();
                }while(burstTime != -1 && arrTime != -1 && priority != -1);
            }
            else if(inputMode == 2){
                c.addToProcessList(new Process(0, 6, 0, 3));
                c.addToProcessList(new Process(1, 4, 1, 3));
                c.addToProcessList(new Process(2, 6, 5, 1));
                c.addToProcessList(new Process(3, 6, 6, 1));
                c.addToProcessList(new Process(4, 6, 7, 4));
                c.addToProcessList(new Process(5, 6, 8, 5));
            }
            if(c.getProcessList().isEmpty())
                System.exit(0);
        }while(inputMode<1 || inputMode>2);    

        while(processing){
            processing = c.isProcessing(time);
            time++;
        }
        c.calTurnaroundTimeAndWaitingTime();

        drawGanttChart(c);
        System.out.println();
        System.out.println();
        drawTable(c);
    }

    public void drawGanttChart(CpuScheduling c){
        List<Integer> startBurstTimeAndArrivalTimeList = c.getStartBurstTimeAndArrivalTimeList();
        Collections.sort(startBurstTimeAndArrivalTimeList);
        int num = 0;
        int priority = 0;
        int stopNum = 0;
        int burstTime = 0;
        int arriveNum = 0;
        int timeNum = 0;
        int maxNumOfSameArrivalTime = 1;
        int size = startBurstTimeAndArrivalTimeList.size();        

        for(int i = 0 ; i < size ; i++){
            int time = startBurstTimeAndArrivalTimeList.get(i);
            Process stoppedProcess = c.getProcessByStopBurstTime(time);
            if(stoppedProcess != null){
                priority = stoppedProcess.getPriority();
                stopNum = stoppedProcess.getNum();
                burstTime = stoppedProcess.getBurstTimeAtTime(time);
                System.out.printf("P%d(%-2d)^%d   ",stopNum, burstTime, priority);
            }
            else{
                System.out.print("           ");
            }
        }
        System.out.println();

        for(int i = 0 ; i < size ; i++){
            Process startedProcess = c.getProcessByStartBurstTime(startBurstTimeAndArrivalTimeList.get(i));
            if(startedProcess != null)
                System.out.print("+----------");
            else
                System.out.print("-----------");
        }
        System.out.println("+");

        for(int i = 0 ; i < size ; i++){
            Process startedProcess = c.getProcessByStartBurstTime(startBurstTimeAndArrivalTimeList.get(i));
            if(startedProcess != null){
                num = startedProcess.getNum();
                System.out.print("| P" + num + "       ");
            }
            else
                System.out.print("           ");
        }
        System.out.println("|");

        for(int i = 0 ; i < size ; i++){
            System.out.print("+----------");
        }
        System.out.println("+");

        for(int i = 0 ; i < size ; i++){
            timeNum = startBurstTimeAndArrivalTimeList.get(i);
            System.out.printf("%-2d         ", timeNum);
        }
        System.out.println(c.getProcessByStartBurstTime(startBurstTimeAndArrivalTimeList.get(size-1)).getFinishingTime());

        for(int i = 0 ; i < size ; i++){
            List<Process> arrivedProcess = c.getProcessListByArrivalTime(startBurstTimeAndArrivalTimeList.get(i));
            if(!arrivedProcess.isEmpty())
                System.out.print("|          ");
            else
                System.out.print("           ");
        }
        System.out.println();
        
        for(int i = 0 ; i < maxNumOfSameArrivalTime ; i++){
            for(int j = 0 ; j < size ; j++){
                List<Process> arrivedProcess = c.getProcessListByArrivalTime(startBurstTimeAndArrivalTimeList.get(j));

                if(!arrivedProcess.isEmpty() && arrivedProcess.size() > i){
                    if(maxNumOfSameArrivalTime < arrivedProcess.size()){
                        maxNumOfSameArrivalTime = arrivedProcess.size();
                    }
                    priority = arrivedProcess.get(i).getPriority();
                    arriveNum = arrivedProcess.get(i).getNum();
                    burstTime = arrivedProcess.get(i).getOrgBurstTime();
                    System.out.printf("P%d(%-2d)^%d   ",arriveNum, burstTime, priority);
                }
                else
                    System.out.print("           ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void drawTable(CpuScheduling c){
        List<Process> processList = c.getProcessList();
        System.out.println("+---------+--------------+--------------+----------------+-----------------+--------------+");
        System.out.println("| PROCESS | ARRIVAL TIME |  BURST TIME  | FINISHING TIME | TURNAROUND TIME | WAITING TIME |");
        System.out.println("+---------+--------------+--------------+----------------+-----------------+--------------+");
        for(Process p : processList){
            System.out.printf("|   P%d    |      %2d      |      %2d      |       %2d       |       %2d        |      %2d      |\n", 
                            p.getNum(), p.getArrivalTime(), p.getOrgBurstTime(), p.getFinishingTime(), p.getTurnaroundTime(), p.getWaitingTime());
        }
        System.out.println("+---------+--------------+--------------+----------------+-----------------+--------------+");
        
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        double averageWaitingTime = 0;
        double averageTurnaroundTime = 0;
        
        System.out.println();
        for(Process p : processList){
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
        }
        averageWaitingTime = (double)(totalWaitingTime)/(Process.getNumOfProcess());
        averageTurnaroundTime = (double)(totalTurnaroundTime)/(Process.getNumOfProcess());
        
        System.out.println("Total Turnaround Time: " + totalTurnaroundTime + " and Average Turnaround Time: " + averageTurnaroundTime);
        System.out.println("Total Waiting Time: " + totalWaitingTime + " and Average Waiting Time: " + averageWaitingTime);
        
    }
}