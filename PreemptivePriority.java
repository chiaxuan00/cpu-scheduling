import java.util.*;

public class PreemptivePriority implements CpuScheduling{
    private Process processToBeProcessed;
    private Process prevProcess = null;

    private List<Process> processList;
    private List<Process> processArrived;
    private List<Process> processWaiting;
    private List<Process> processWithHighestPriority;
    private List<Process> processWithShortestBurstTime;
    private List<Process> processWithLongestQueueTime;
    private List<Integer> startBurstTimeAndArrivalTimeList;

    public PreemptivePriority(){
       processList = new ArrayList<>();
       processArrived = new ArrayList<>();
       processWaiting = new ArrayList<>();
       processWithHighestPriority = new ArrayList<>();
       processWithShortestBurstTime = new ArrayList<>();
       processWithLongestQueueTime = new ArrayList<>();
       startBurstTimeAndArrivalTimeList = new ArrayList<>();
    }

    public boolean isProcessing(int time){
       
        if(processWaiting.size() == 0 && processArrived.size() == Process.getNumOfProcess())  //stop looping
            return false;

        addProcessArrived(time);
        addProcessWaiting(processArrived);                    
        addProcessWithHighestPriority(processWaiting);

        if(processWithHighestPriority.size() == 1){      
            processToBeProcessed = processWithHighestPriority.get(0);
        }            
        else if(processWithHighestPriority.size() > 1){  //if got 2 or more processes have same priority  
            addProcessWithLongestQueueTime(processWithHighestPriority, time);
            if(processWithLongestQueueTime.size() == 1){  
                processToBeProcessed = processWithLongestQueueTime.get(0);
            }    
            else{                                        //if got 2 or more processes have same arrivalTime
                processToBeProcessed = findProcessWithShortestBurstTime(processWithLongestQueueTime);
            }
        }
        
        processWithHighestPriority.clear();
        processWithLongestQueueTime.clear();

        if(prevProcess == null){  //di yi ge or shang yi ge wan liao
            processToBeProcessed.addStartBurstTime(time);
            addWithoutDuplicate(startBurstTimeAndArrivalTimeList, time);
            processToBeProcessed.burst(time);
        }
        else if(prevProcess.equals(processToBeProcessed)){  //ji xu na ge process
            processToBeProcessed.burst(time);
        } 
        else{  //you ren cha dui
            prevProcess.addStopBurstTime(time);
            processToBeProcessed.addStartBurstTime(time);
            addWithoutDuplicate(startBurstTimeAndArrivalTimeList, time);
            processToBeProcessed.burst(time);
        }

        if(processToBeProcessed.getBurstTime() == 0){   //burst done le
            processToBeProcessed.setBurstDone(true);
            processToBeProcessed.setFinishingTime(time + 1);
            processWaiting.remove(processToBeProcessed);
        }

        prevProcess = processToBeProcessed;
        if(prevProcess.hasDoneBurst()){
            prevProcess = null;
        }
            
        return true;
    }

    private void addProcessArrived(int time){
        for(Process p : processList){
            if(p.getArrivalTime() == time && !processArrived.contains(p)){
                addWithoutDuplicate(startBurstTimeAndArrivalTimeList, time);
                processArrived.add(p);
            }
        }
    }

    private void addProcessWaiting(List<Process> pList){
        for(Process p : pList){
            if(!p.hasDoneBurst() && !processWaiting.contains(p)){
                processWaiting.add(p);
            }
        }
    }

    private void addProcessWithHighestPriority(List<Process> pList){
        int max = pList.get(0).getPriority();
        
        for(Process p : pList){
            if(max > p.getPriority()){
                max = p.getPriority();
            }
        }

        for(Process p : pList){
            if(max == p.getPriority()){
                processWithHighestPriority.add(p);
            }
        }
    }
    
    private void addProcessWithShortestBurstTime(List<Process> pList){
        int min = pList.get(0).getBurstTime();
        
        for(Process p : pList){
            if(min > p.getBurstTime()){
                min = p.getBurstTime();
            }
        }

        for(Process p : pList){
            if(min == p.getBurstTime()){
                processWithShortestBurstTime.add(p);
            }
        }
    }

    private void calProcessQueueTime(Process p, int time){
        if(!p.getStopBurstTimeList().isEmpty()){
            p.setQueueTime(time - p.getStopBurstTimeList().get(p.getStopBurstTimeList().size() - 1));
        }
            
        else{
            p.setQueueTime(time - p.getArrivalTime());
        } 
    }

    private void addProcessWithLongestQueueTime(List<Process> pList, int time){
        for(Process p : pList){
            calProcessQueueTime(p, time);
        }

        int max = pList.get(0).getQueueTime();

        for(Process p : pList){
            if(max < p.getQueueTime()){
                max = p.getQueueTime();
            }
        }

        for(Process p : pList){
            if(max == p.getQueueTime()){
                processWithLongestQueueTime.add(p);
            }
        }
    }
    
    private Process findProcessWithShortestBurstTime(List<Process> pList){
        if(pList.size() <= 1)
            return null;

        int min = pList.get(0).getBurstTime();
        Process minP = pList.get(0);
        for(Process p : pList){
            if(min < p.getBurstTime()){
                min = p.getBurstTime();
                minP = p;
            }
        }
        return minP;
    }
    
    private Process findProcessWithHighestPriority(List<Process> pList){
        if(pList.size() <= 1)
            return null;

        int max = pList.get(0).getPriority();
        Process maxP = pList.get(0);
        for(Process p : pList){
            if(max < p.getPriority()){
                max = p.getPriority();
                maxP = p;
            }
        }
        return maxP;
    }
   
    public void addWithoutDuplicate(List<Integer> list, int i){
        if(!list.contains(i))
            list.add(i);
    }

    public List<Integer> getStartBurstTimeAndArrivalTimeList(){
        Collections.sort(startBurstTimeAndArrivalTimeList);
        return startBurstTimeAndArrivalTimeList;
    }
    
    public Process getProcessByStartBurstTime(int startBurstTime){
        for(Process p : processList){
            if(p.getStartBurstTimeList().contains(startBurstTime))
                return p;
        }
        return null;
    }  
    
    public Process getProcessByStopBurstTime(int stopBurstTime){
        for(Process p : processList){
            if(p.getStopBurstTimeList().contains(stopBurstTime))
                return p;
        }
        return null;
    } 
   
    public List<Process> getProcessListByArrivalTime(int arrivalTime){
        List<Process> pList = new ArrayList<>();
        for(Process p : processList){
            if(p.getArrivalTime() == arrivalTime)
                pList.add(p);
        }
        return pList;
    } 

    public void addToProcessList(Process process){
        processList.add(process);
    }

    public List<Process> getProcessList(){
        return processList;
    }
    
    public void calTurnaroundTimeAndWaitingTime(){
        for(Process p : processList){
            p.setTurnaroundTime(p.getFinishingTime() - p.getArrivalTime());
            p.setWaitingTime(p.getTurnaroundTime() - p.getOrgBurstTime());
        }
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        for(Process p : processList){
            s.append(p.toString() + "\n");
        }
        return s.toString();
    }

}