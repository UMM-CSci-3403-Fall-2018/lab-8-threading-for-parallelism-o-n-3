package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    // Create two variables to be used throughout the program: globalResult is the smalled distance, and values is the initial list passed in.
    public long globalResult = Integer.MAX_VALUE;
    public int[] values;

    // Function to update the globalResult variable. Checks if argument is smaller than current min, and if it is, replace it.
    public synchronized void updateGlobalResults(long localResult)
    {
        // Checks if argument is smaller than current min, and if it is, replace it.
        if(localResult < globalResult)
        {
            globalResult = localResult;
        }
    }

    @Override
    public long minimumPairwiseDistance(int[] values) {

        // Essentially makes the values array accessible anywhere in the class
        this.values = values;

        //Create an array of threads
        Thread[] threads = new Thread[4];

        //Add a newly created threat into our array of threads, where each thread is responsible for a section of searching.
        threads[0] = new Thread(new BottomLeft());
        threads[1] = new Thread(new BottomRight());
        threads[2] = new Thread(new UpperRight());
        threads[3] = new Thread(new Middle());

        //Start all the threads
        for (int i = 0; i < 4; i++)
        {
            threads[i].start();
        }

        //Wait for all the threads to finish
        for (int i = 0; i < 4; i++)
        {
            try
            {
                threads[i].join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        //Return outcome
        return globalResult;
    }

    //Each of the logic for the four sections of triangles are the same. Only this first will be commented
    public class BottomLeft implements Runnable {
        //Create a variable to keep track of this sections lowest pair.
        long localResult = Integer.MAX_VALUE;

        public void run()
        {
            int n = values.length;

            //These nested for-loops construct the bounds of the triangle as we 'move' across the values
            for(int i = 0; i < n/2; i++)
            {
                for(int j = 0; j < i; j++)
                {
                    //Calculate the distance between the two
                    long new_value = Math.abs(values[i] - values[j]);

                    //If that distance is lower than the local lowest, change the local lowest to be the new smallest distance
                    if(new_value < localResult)
                    {
                        localResult = new_value;
                    }
                }
            }

            //Check the global minimum to see if we found smaller.
            updateGlobalResults(localResult);
        }
    }

    public class BottomRight implements Runnable {
        long localResult = Integer.MAX_VALUE;

        public void run() {
            int n = values.length;

            for(int i = n/2; i < n; i++)
            {
                for(int j = 0; j + n/2 < i; j++)
                {
                    long new_value = Math.abs(values[i] - values[j]);

                    if(new_value < localResult)
                    {
                        localResult = new_value;
                    }
                }
            }

            updateGlobalResults(localResult);
        }
    }

    public class UpperRight implements Runnable {
        long localResult = Integer.MAX_VALUE;

        public void run() {
            int n = values.length;

            //questionable
            for(int i = n/2; i < n; i++)
            {
                for(int j = n/2; j < i; j++)
                {
                    long new_value = Math.abs(values[i] - values[j]);

                    if(new_value < localResult)
                    {
                        localResult = new_value;
                    }
                }
            }

            updateGlobalResults(localResult);
        }
    }

    public class Middle implements Runnable {
        long localResult = Integer.MAX_VALUE;

        public void run() {
            int n = values.length;

            for(int j = 0; j < n - n/2; j++)
            {
                for(int i = n/2; i <= j + n/2; i++)
                {
                    long new_value = Math.abs(values[i] - values[j]);

                    if (new_value < localResult)
                    {
                        localResult = new_value;
                    }
                }
            }

            updateGlobalResults(localResult);
        }
    }
}
