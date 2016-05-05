import java.util.*;
import java.util.stream.Stream;

class User {
    String name;
    ArrayList<String> interests;
}

class Interest {
    String name;
    ArrayList<String> users;
}

class Node {
    int val;
    String name;
}

class Dijkstra {
    static final int MAX = 999999;

    int N, M;
    int[][] E;

    Dijkstra(int N, int M, int[][] E) {
        this.N = N;
        this.M = M;
        this.E = E;
    }

    int getMinimum(int[] d, int[] v, int N)
    {
        int min = 0;
        for(int i = 0; i < N; i++)
            if(v[i] == 0)
            {
                min = i;
                break;
            }

        for(int i = 0; i < N; i++)
            if(v[i] == 0 && d[i] < d[min])
                min = i;

        return min;
    }

    boolean someNodesUnvisited(int[] visited, int N)
    {
        for(int i = 0; i < N; i++)
            if(visited[i] == 0)
                return true;
        return false;
    }

    int getDistanceBetween(int src, int dst) {
        int[] d = new int[N], visited = new int[N];
        for(int k = 0; k < N; k++)
            d[k] = MAX;
        d[src] = 0;

        int u, v;
        while(someNodesUnvisited(visited, N))
        {
            u = getMinimum(d, visited, N);
            visited[u] = 1;

            for(v = 0; v < N; v++)
                if(E[u][v] != 0 && d[u] + E[u][v] < d[v])
                    d[v] = d[u] + E[u][v];
        }

        return d[dst];
    }
}

public class Solution {

    static final int MAX = 999999;
    static final int INF = 99999999;

    public static void main(String[] args) {
        int noOfUsers;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter no of users :");
        noOfUsers = sc.nextInt();
        User[] usersTable = new User[noOfUsers + 1];

        for (int i = 1; i <= noOfUsers; i++) {
            usersTable[i] = new User();
            usersTable[i].interests = new ArrayList<String>();
        }

   // Users input dynamically
      for(int i=1; i <= noOfUsers; i++)
        {
            String name = "U" + i;
            usersTable[i].name = name;
        }
        /*
//        usersTable[0].name = "U0";

       usersTable[1].name = "U1";
        usersTable[2].name = "U2";
        usersTable[3].name = "U3";
        usersTable[4].name = "U4";
        usersTable[5].name = "U5";
        usersTable[6].name = "U6";
        usersTable[7].name = "U7";
        usersTable[8].name = "U8";
        usersTable[9].name = "U9";
*/
   //Code for taking interests of users dynamic
      for(int i=1; i <= noOfUsers; i++)
        {
            System.out.println("Enter the no of interests of user  "+i+" :");
            int no = sc.nextInt();
            for(int j = 1; j <= no; j++)
            {
                System.out.println("Enter the " +j+"th interest of user "+i+" :");
                String intr = sc.next();
                usersTable[i].interests.add(intr);
            }
           // System.out.println(usersTable[1].interests);
        }
    /*
        usersTable[1].interests.add("A1");
        usersTable[1].interests.add("A2");
        usersTable[1].interests.add("A5");

        usersTable[2].interests.add("A1");
        usersTable[2].interests.add("A3");
        usersTable[2].interests.add("A4");
//        usersTable[2].interests.add("A5");

        usersTable[3].interests.add("A1");

        usersTable[4].interests.add("A6");

        usersTable[5].interests.add("A3");
        usersTable[5].interests.add("A4");

        usersTable[6].interests.add("A2");
        usersTable[6].interests.add("A5");

        usersTable[7].interests.add("A2");

        usersTable[8].interests.add("A3");

        usersTable[9].interests.add("A1");
        */

        HashSet<String> interestsHashSet = new HashSet<String>();

        for (int i = 1; i <= noOfUsers; i++)
            if (usersTable[i].interests.size() > 0)
                for (String stringInterest : usersTable[i].interests)
                    interestsHashSet.add(stringInterest);

        System.out.print(interestsHashSet);

        Interest[] interestsTable = new Interest[interestsHashSet.size()];
        for (int i = 0; i < interestsHashSet.size(); i++) {
            interestsTable[i] = new Interest();
            interestsTable[i].users = new ArrayList<String>();
        }

        int temp = 0;
        for (String stringInterest : interestsHashSet)
            interestsTable[temp++].name = stringInterest;

        for (int i = 1; i <= noOfUsers; i++) {
            for (String stringInterest : usersTable[i].interests) {
                for (Interest interestObject : interestsTable)
                    if (interestObject.name.equals(stringInterest))
                        interestObject.users.add(usersTable[i].name);
            }
        }

        System.out.println();
        for (Interest interestObject : interestsTable) {
            System.out.print(interestObject.name + " -> ");
            for (String username : interestObject.users)
                System.out.print(username + " ");
            System.out.println();
        }

        HashMap<ArrayList<String>, Integer> FI = new HashMap<>();
        HashMap<ArrayList<String>, Integer> CI = new HashMap<>();
        //HashMap<ArrayList<String>, Integer> cachedFI = null;

        System.out.println("Enter the value of minSupport : ");
        int minSupport = sc.nextInt();

        System.out.println("Apriori started...");

        for (int i = 1; i <= noOfUsers; i++) {
            ArrayList<String> users = new ArrayList<>();
            users.add(usersTable[i].name);
            CI.put(users, 0);
        }

        for (Interest interestObject : interestsTable) {
            ArrayList<String> usersInInterestObject = interestObject.users;
            for (ArrayList<String> usersCI : CI.keySet())
                if (usersInInterestObject.containsAll(usersCI))
                    CI.put(usersCI, CI.get(usersCI) + 1);
        }

        for (ArrayList<String> usersCI : CI.keySet())
            if (CI.get(usersCI) >= minSupport)
                FI.put(usersCI, CI.get(usersCI));

        int nextSize = 2;
        while (FI.size() > 1) {

            System.out.println("CI entry -> " + CI);
            System.out.println("FI entry -> " + FI);
            //System.out.println("cachedFI entry -> " + cachedFI);

            CI.clear();
            ArrayList<ArrayList<String>> al = new ArrayList<ArrayList<String>>(FI.keySet());
            for (int i = 0; i < al.size(); i++) {
                for (int j = i + 1; j < al.size(); j++) {
                    ArrayList<String> al1 = al.get(i);
                    ArrayList<String> al2 = al.get(j);

                    HashSet<String> hs = new HashSet<String>();
                    for (String username : al1)
                        hs.add(username);
                    for (String username : al2)
                        hs.add(username);

                    if (hs.size() == nextSize) {
                        ArrayList<String> arrayListMerged = new ArrayList<String>(hs);
                        CI.put(arrayListMerged, 0);
                    }

                }
            }

            for (Interest interestObject : interestsTable) {
                ArrayList<String> usersInInterestObject = interestObject.users;
                for (ArrayList<String> usersCI : CI.keySet())
                    if (usersInInterestObject.containsAll(usersCI))
                        CI.put(usersCI, CI.get(usersCI) + 1);
            }

            FI.clear();

            for (ArrayList<String> usersCI : CI.keySet())
                if (CI.get(usersCI) >= minSupport)
                    FI.put(usersCI, CI.get(usersCI));

            //cachedFI = (HashMap<ArrayList<String>, Integer>) FI.clone();

            System.out.println("CI exit -> " + CI);
            System.out.println("FI exit -> " + FI);
            //System.out.println("cachedFI exit -> " + cachedFI);

            nextSize++;
        }
        System.out.println("Apriori finished...");

        //printCachedFI(cachedFI);

        HashMap<ArrayList<String>, Boolean> groups = new HashMap<ArrayList<String>, Boolean>();
        for (ArrayList<String> group : FI.keySet())
            groups.put(new ArrayList<String>(group), true);

        printGroups(groups);

        int N = noOfUsers;
        System.out.println("Enter the no of edges in the graph : ");
        int M = sc.nextInt();

       // int M = 9;
        int[][] EforDijkstra = new int[N][N];
        int[][] EforWarshall = new int[N][N];

        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++)
                if(i == j)
                    EforWarshall[i][j] = 0;
                else
                    EforWarshall[i][j] = INF;
  //Code for taking edges dynamically from keyboard
      for(int i = 0; i< M ;i++)
        {
            System.out.println("Enter the "+i+"edge :");

            int x = sc.nextInt();
            int y = sc.nextInt();
            EforWarshall[x][y] = EforWarshall[y][x] = 1;
            EforDijkstra[x][y] = EforDijkstra[y][x] = 1;
        }

    /*    E[0][5] = E[5][0] = 1;
        E[5][2] = E[2][5] = 1;
        E[2][4] = E[4][2] = 1;
        E[0][2] = E[2][0] = 1;
        E[7][2] = E[2][7] = 1;
        E[2][3] = E[3][2] = 1;
        E[3][4] = E[4][3] = 1;
        E[3][6] = E[6][3] = 1;
        E[1][6] = E[6][1] = 1;

*/
        //System.out.println(cachedFI);
        HashMap<String, Integer> nameToIDMap = new HashMap<>();
        HashMap<Integer, String> idToNameMap = new HashMap<>();
        for (int i = 1; i <= noOfUsers; i++) {
            nameToIDMap.put(usersTable[i].name, i - 1);
            idToNameMap.put(i-1, usersTable[i].name);
        }

        System.out.println("Enter the value of Beta :");
        int alpha = sc.nextInt();
        validateGroups(groups, alpha, nameToIDMap, N, M, EforDijkstra);

        while(!areAllGroupsValid(groups)) {
            System.out.println("While entered...");
            System.out.println("groups while entering while... " + groups);

            for (ArrayList<String> group : groups.keySet()) {
                if (groups.get(group) == false) {
                    //prune
                    ArrayList<String> groupToBePruned = new ArrayList<String>(group);
                    groups.remove(group);
                    for (int i = 0; i < groupToBePruned.size(); i++) {
                        for (int j = i + 1; j < groupToBePruned.size(); j++) {
                            int src = nameToIDMap.get(groupToBePruned.get(i));
                            int dst = nameToIDMap.get(groupToBePruned.get(j));

                            int distance = new Dijkstra(N, M, EforDijkstra).getDistanceBetween(src, dst);
//                            System.out.println(src + " <--> " + dst + " : " + distance);

                            if (distance == -1 || distance > alpha) {
                                //pruned
//                                System.out.println(src + " " + dst);
                                ArrayList<String> g1 = new ArrayList<String>();
                                ArrayList<String> g2 = new ArrayList<String>();

                                g1.add(idToNameMap.get(src));
                                g2.add(idToNameMap.get(dst));

                                for(String username : groupToBePruned) {
                                    int currNode = nameToIDMap.get(username);
                                    if(currNode != src && currNode != dst) {
                                        Dijkstra dijkstra = new Dijkstra(N, M, EforDijkstra);

                                        int d1 = dijkstra.getDistanceBetween(currNode ,src);
                                        int d2 = dijkstra.getDistanceBetween(currNode, dst);

                                        if(d1 < d2)
                                            g1.add(idToNameMap.get(currNode));
                                        else
                                            g2.add(idToNameMap.get(currNode));
                                    }
                                }

                                Collections.sort(g1);
                                Collections.sort(g2);

                                groups.put(g1, true);
                                groups.put(g2, true);
                            }
                        }
                    }
                }
            }

            System.out.println("groups while exiting while... " + groups);
            System.out.println("While exited...");
        }

        System.out.println(groups);

        //find the users which are members of any community
        HashSet<String> usersOfGroups = new HashSet<String>();
        for(ArrayList<String> group : groups.keySet())
            for(String username : group)
                usersOfGroups.add(username);

        //find the users who are not members of any community
        HashSet<String> unassignedUsers = new HashSet<String>();
        for(int i = 1; i <= noOfUsers; i++)
            if(!usersOfGroups.contains(usersTable[i].name))
                unassignedUsers.add(usersTable[i].name);

        System.out.println(unassignedUsers);
        int[][] sol = new int[N][N];
        floydWarshall(EforWarshall, sol, N);

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++)
                if(sol[i][j] == INF) {
                    sol[i][j] = -1;
                    System.out.print(sol[i][j] + " ");
                }

                else
                    System.out.print(sol[i][j] + " ");
            System.out.println();
        }

        ArrayList<String> newGroupsList = new ArrayList<String>();
        HashMap<ArrayList<String>, Float> distanceMap = new HashMap<ArrayList<String>, Float>();
        for(String unassignedUsername : unassignedUsers) {
            int id = nameToIDMap.get(unassignedUsername);
            int distanceToAllGroups = 0;
            for(ArrayList<String> group : groups.keySet()) {
                int totalDistance = 0;
                for(String username : group) {
                    int src = id;
                    int dst = nameToIDMap.get(username);
                    int distance = new Dijkstra(N, M, EforDijkstra).getDistanceBetween(src, dst);
                    if(distance > 0) {
                        totalDistance += distance;
                        distanceToAllGroups += distance;
                    }


                }
                float avgDistace = (float) totalDistance / group.size();
                System.out.println("Distance of " + unassignedUsername + " from " + group + " is: " + avgDistace);
                distanceMap.put(group, avgDistace);
            }

            if(distanceToAllGroups == 0) {
                newGroupsList.add(unassignedUsername);
            }
            else {
                float min = Float.MAX_VALUE;
                for(ArrayList<String> group : distanceMap.keySet()) {
                    if(distanceMap.get(group) < min && distanceMap.get(group) != -1)
                        min = distanceMap.get(group);
                }
                for(ArrayList<String> group : distanceMap.keySet()) {
                    if(distanceMap.get(group) == min) {
                        ArrayList<String> newGroup = new ArrayList<String>(group);
                        newGroup.add(unassignedUsername);
                        groups.remove(group);
                        groups.put(newGroup, true);
                    }
                }
            }

            System.out.println(distanceMap);
            distanceMap.clear();
        }

        for(String username : newGroupsList) {
            ArrayList<String> newAL = new ArrayList<>();
            newAL.add(username);
            groups.put(newAL, true);
        }

        System.out.println(groups);
    }

    static void validateGroups(HashMap<ArrayList<String>, Boolean> groups, int alpha, HashMap<String, Integer> nameToIDMap, int N, int M, int[][] E) {
        for (ArrayList<String> group : groups.keySet()) {
            boolean isValidGroup = true;
            for (int i = 0; i < group.size(); i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    int src = nameToIDMap.get(group.get(i));
                    int dst = nameToIDMap.get(group.get(j));

                    int distance = new Dijkstra(N, M, E).getDistanceBetween(src, dst);

                    if (distance == -1 || distance > alpha) {
                        //pruned
                        System.out.println(src + " <--> " + dst + " : " + distance);
                        System.out.println(group + " is not a valid group because of " + src + " and " + dst);
                        isValidGroup = false;
                        groups.put(group, false);
                        break;
                    }
                }
                if (!isValidGroup)
                    break;
            }
            if (isValidGroup)
                System.out.println(group + " is a valid group");
        }
    }

    static boolean areAllGroupsValid(HashMap<ArrayList<String>, Boolean> groups) {
        for(boolean isValidGroup : groups.values())
            if(isValidGroup == false)
                return false;
        return true;
    }

    static void floydWarshall(int[][] G, int[][] sol, int V)
    {
        for(int i = 0; i < V; i++)
            for (int j = 0; j < V; j++)
                sol[i][j] = G[i][j];

        for(int k = 0; k < V; k++)
            for(int i = 0; i < V; i++)
                for(int j = 0; j < V; j++)
                    if(sol[i][k] + sol[k][j] < sol[i][j])
                        sol[i][j] = sol[i][k] + sol[k][j];
    }

    static void printGroups(HashMap<ArrayList<String>, Boolean> groups) {
        System.out.println("groups -> " + groups);
    }

    static void printCachedFI(HashMap<ArrayList<String>, Integer> cachedFI) {
        System.out.println("cachedFI -> " + cachedFI);
    }
}