package jerco.scenarios;

public interface ScenarioProgressIndicator {
    /**
     * Реализует шаблон нулевого объекта. Определяет пустые методы индикатора.
     */
    public final ScenarioProgressIndicator NULL_INDICATOR = new ScenarioProgressIndicator() {
        @Override
        public void init(int count) {
        }

        @Override
        public void progress(int position) {
        }

        @Override
        public void done() {
        }
    };
    
    /**
     * Простейшая реализация индикатора. Информация выводится на консоль используя
     * метод System.out.println();
     */
    public final ScenarioProgressIndicator SYSTEM_OUT_INDICATOR = new ScenarioProgressIndicator(){
        private int count;
        
        @Override
        public void init(int count) {
            this.count = count;
        }

        @Override
        public void progress(int position) {
            System.out.printf("%03d/%03d\n",position,count);
        }

        @Override
        public void done() {
            System.out.println("data done");
        }
        
    };
    
    /**
     * Первый методы, который вызывается до запуска всех сценариев. Передается
     * количество экспериментов в сценарии.
     * 
     * @param count
     *          количество экспериментов в сценарии
     */
    public void init(int count);

    /**
     * Метод вызывается после выполнения каждого сценария.
     * 
     * @param position
     */
    public void progress(int position);
    
    /**
     * Метод вызывается при завершение работы сценария
     */
    public void done();
}


