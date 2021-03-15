import java.util.*;

public interface CpuScheduling{

    public boolean isProcessing(int time);
    public void addToProcessList(Process process);
    
    public void calTurnaroundTimeAndWaitingTime();

    public List<Process> getProcessList();
    public List<Integer> getStartBurstTimeAndArrivalTimeList();
    public Process getProcessByStartBurstTime(int startBurstTime);
    public Process getProcessByStopBurstTime(int stopBurstTime);
    public List<Process> getProcessListByArrivalTime(int arrivalTime);    

    public String toString();
}