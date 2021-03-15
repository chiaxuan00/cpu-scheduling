import java.util.Scanner;

public class Run {
    public static void main(String[] args){
        int schedulingType;
        CpuScheduling cpuScheduling = null;

        System.out.println("What CPU scheduling type you want?");
        System.out.println("1. Non Preemptive SJF");
        System.out.println("2. Preemptive SJF");
        System.out.println("3. Preemptive Priority");

        try(Scanner input = new Scanner(System.in)){
            schedulingType = input.nextInt();

            while(schedulingType < 1 || schedulingType > 3){
                System.out.println("Choose only 1/2/3!");
                schedulingType = input.nextInt();
            }
        }

        switch(schedulingType){
            case 1:
                cpuScheduling = new NonPreemptiveSJF();
                break;
            case 2:
                cpuScheduling = new PreemptiveSJF();
                break;
            case 3:
                cpuScheduling = new PreemptivePriority();
                break;
        }

        if(cpuScheduling != null)
            new Main(cpuScheduling);
    }
}