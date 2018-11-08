package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    @Override
    public long minimumPairwiseDistance(int[] values) {
        Answer answer = new Answer();

        Thread[] threads = new Thread[4];
        threads[0] = new Thread(new BottomLeft(values, answer));
        threads[1] = new Thread(new BottomRight(values, answer));
        threads[2] = new Thread(new UpperRight(values, answer));
        threads[3] = new Thread(new Middle(values, answer));

        for (int i = 0; i < 4; i++) {
            threads[i].start();
        }

        for (int i = 0; i < 4; i++) {
            try {
                threads[i].join();
                System.out.println("Thread " + i + " was joined.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return answer.getSmallestDifference();
    }

    private class BottomLeft implements Runnable {
        int[] values;
        Answer answer;

        public BottomLeft(int[] values, Answer answer) {
            this.values = values;
            this.answer = answer;
        }

        public void run() {

            int n = values.length;

            for(int i = 0; i < n/2; i++)
            {
                for(int j = 0; j < i; j++)
                {
                    int new_value = Math.abs(values[i] - values[j]);

                    if(new_value < answer.getSmallestDifference())
                    {
                        answer.setSmallestDifference(new_value);
                    }
                }
            }
        }
    }

    private class BottomRight implements Runnable {
        int[] values;
        Answer answer;

        public BottomRight(int[] values, Answer answer) {
            this.values = values;
            this.answer = answer;
        }

        public void run() {
            int n = values.length;

            for(int i = n/2; i < n; i++)
            {
                for(int j = 0; j + n/2 < i; j++)
                {
                    int new_value = Math.abs(values[i] - values[j]);

                    if(new_value < answer.getSmallestDifference())
                    {
                        answer.setSmallestDifference(new_value);
                    }
                }
            }
        }
    }

    private class UpperRight implements Runnable {
        int[] values;
        Answer answer;

        public UpperRight(int[] values, Answer answer) {
            this.values = values;
            this.answer = answer;
        }
        public void run() {
            int n = values.length;

            //questionable
            for(int i = n/2; i < n; i++)
            {
                for(int j = n/2; j < i; j++)
                {
                    int new_value = Math.abs(values[i] - values[j]);

                    if(new_value < answer.getSmallestDifference())
                    {
                        answer.setSmallestDifference(new_value);
                    }
                }
            }
        }
    }

    private class Middle implements Runnable {
        int[] values;
        Answer answer;

        public Middle(int[] values, Answer answer) {
            this.values = values;
            this.answer = answer;
        }
        public void run() {
            int n = values.length;

            for(int j = 0; j < n/2; j++)
            {
                for(int i = j; i < n/2; i++)
                {
                    int new_value = Math.abs(values[i] - values[j]);

                    if(new_value < answer.getSmallestDifference())
                    {
                        answer.setSmallestDifference(new_value);
                    }
                }
            }
        }
    }

    private class Answer {
        private int smallestDifference = Integer.MAX_VALUE;

        public Answer() {}

        public synchronized void setSmallestDifference(int newSmallest) {
            this.smallestDifference = newSmallest;
        }

        public synchronized int getSmallestDifference() {
            return this.smallestDifference;
        }
    }

}
