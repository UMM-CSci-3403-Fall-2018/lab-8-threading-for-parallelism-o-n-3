package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    public long globalResult = Integer.MAX_VALUE;
    public int[] values;

    public synchronized void updateGlobalResults(long localResult)
    {
        if(localResult < globalResult)
        {
            globalResult = localResult;
        }
    }

    @Override
    public long minimumPairwiseDistance(int[] values) {

        this.values = values;

        Thread[] threads = new Thread[4];
        threads[0] = new Thread(new BottomLeft());
        threads[1] = new Thread(new BottomRight());
        threads[2] = new Thread(new UpperRight());
        threads[3] = new Thread(new Middle());

        for (int i = 0; i < 4; i++)
        {
            threads[i].start();
        }

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

        return globalResult;
    }

    public class BottomLeft implements Runnable {
        long localResult = Integer.MAX_VALUE;

        public void run()
        {
            int n = values.length;

            for(int i = 0; i < n/2; i++)
            {
                for(int j = 0; j < i; j++)
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
