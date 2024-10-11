import java.util.*;

public class Client {
    private static Random rand = new Random();

    public static void main(String[] args) throws Exception {
        // List<Location> scenario = createRandomScenario(10, 10, 100, 1000, 100000);
        List<Location> scenario = createSimpleScenario();
        System.out.println(scenario);
        
        double budget = 2000;
        Allocation allocation = allocateRelief(budget, scenario);
        printResult(allocation, budget);
    }

    // This method takes a budget and a list of Location objects as parameter. 
    // The method will compute and return the allocation of resources that will 
    // result in the most people being helped with the given budget. 
    // If there is more than one allocation that will result in the most people being helped,
    // the method will return the allocation that costs the least. 
    // If there is more than one allocation that will result in 
    // the most people being helped for the lowest cost,
    // the method will return any of these allocations.
    public static Allocation allocateRelief(double budget, List<Location> sites) {
        return allocateRelief(budget, sites, new Allocation());
    }
    
    // Private helper method for allocateRelief
    // Takes in an extra parameter, soFar, to keep track of allocations
    private static Allocation allocateRelief(double budget, 
    List<Location> sites, Allocation soFar) {
        
        Allocation currentAllocation = soFar;

        if (budget > 0) {
            for (int i = 0; i < sites.size(); i++) {
                Location location = sites.get(i); 

                if (budget - location.getCost() >= 0) {
                    
                    // choose
                    sites.remove(i);

                    // explore
                    Allocation result = allocateRelief(budget - location.getCost(),
                    sites, soFar.withLoc(location));

                    // check for new best
                    if (result.totalPeople() > currentAllocation.totalPeople() ||
                        result.totalPeople() == currentAllocation.totalPeople() && 
                        result.totalCost() < currentAllocation.totalCost()) {
                        currentAllocation = result;
                    }

                    // unchoose
                    sites.add(i, location);
                }
            } 
        }
        return currentAllocation;
    }

    // PROVIDED HELPER METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!**

    public static void printResult(Allocation alloc, double budget) {
        System.out.println("Result: ");
        System.out.println("  " + alloc);
        System.out.println("  People helped: " + alloc.totalPeople());
        System.out.printf("  Cost: $%.2f\n", alloc.totalCost());
        System.out.printf("  Unused budget: $%.2f\n", (budget - alloc.totalCost()));
    }

    public static List<Location> createRandomScenario(int numLocs, int minPop, int maxPop, double minCostPer, double maxCostPer) {
        List<Location> result = new ArrayList<>();

        for (int i = 0; i < numLocs; i++) {
            int pop = rand.nextInt(minPop, maxPop + 1);
            double cost = rand.nextDouble(minCostPer, maxCostPer) * pop;
            result.add(new Location("Location #" + i, pop, round2(cost)));
        }

        return result;
    }

    public static List<Location> createSimpleScenario() {
        List<Location> result = new ArrayList<>();

        result.add(new Location("Location #1", 50, 500));
        result.add(new Location("Location #2", 100, 700));
        result.add(new Location("Location #3", 60, 1000));
        result.add(new Location("Location #4", 20, 1000));
        result.add(new Location("Location #5", 200, 900));

        return result;
    }    

    private static double round2(double num) {
        return Math.round(num * 100) / 100.0;
    }
}
