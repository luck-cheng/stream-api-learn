import entity.Dish;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static entity.Dish.menu;
import static java.util.stream.Collectors.*;

/**
 * @author luck
 */
public class Demo {

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.fileStreamFunc();

    }
    /**
     * 筛选操作
     */
    public void filterFunc(){

        // 使用谓词（predicate过滤）filter demo 1
        List<Dish> vegetarianMenu =
                menu.stream()
                        .filter(Dish::isVegetarian)
                        .collect(toList());

        vegetarianMenu.forEach(System.out::println);
        //filter demo 2
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);

        // filter demo 3
        List<Dish> dishesLimit3 =
                menu.stream()
                        .filter(d -> d.getCalories() > 300)
                        .limit(3)
                        .collect(toList());

        dishesLimit3.forEach(System.out::println);

        // filter demo 4
        List<Dish> dishesSkip2 =
                menu.stream()
                        .filter(d -> d.getCalories() > 300)
                        .skip(2)
                        .collect(toList());

        dishesSkip2.forEach(System.out::println);
    }

    /**
     * map数值转换
     */
    public void mapFunc(){
        int calories = menu.stream()
                .mapToInt(Dish::getCalories)
                .sum();
        System.out.println(calories);
    }
    /**
     * map收集流和flatmap扁平收集流区别
     */
    public void flatMapDiffMapFunc(){
        List<String> words = Arrays.asList("Hello", "World");
        // map
        List<String[]> collect1 = words.stream()
                .map(word -> word.split(""))
                .distinct()
                .collect(toList());

        // flatMap
        List<String> collect2 = words.stream()
                .flatMap((String line) -> Arrays.stream(line.split("")))
                .distinct()
                .collect(toList());
        System.out.println(collect1+""+collect2);
    }

    /**
     * reduce归约操作
     */
    public void reduceFunc(){
        List<Integer> numbers = Arrays.asList(1,2,3);
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        System.out.println(sum);

        int sum2 = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum2);

        int max = numbers.stream().reduce(0, (a, b) -> Integer.max(a, b));
//        int max = numbers.stream().reduce(0, (a, b) -> a>b?a:b);
        System.out.println(max);

        Optional<Integer> min = numbers.stream().reduce(Integer::min);
//        Optional<Integer> min = numbers.stream().reduce(0,(a,b)->a>b?b:a);
        min.ifPresent(System.out::println);

        int calories = menu.stream()
                .map(Dish::getCalories)
                .reduce(0, Integer::sum);
        System.out.println(calories);
    }

    /**
     * range生成数据
     */
    public void rangeFunc(){
        IntStream evenNumbers = IntStream.rangeClosed(1, 10)
                .filter(n -> n % 2 == 0);
        evenNumbers.boxed().forEach(System.out::println);
        IntStream evenNumbers2 = IntStream.range(1, 10)
                .filter(n -> n % 2 == 0);
        evenNumbers2.boxed().forEach(System.out::println);


    }

    /**
     * 从文件创建stream
     */
    public void fileStreamFunc(){
        try {
            Files.lines(Paths.get("/Users/admin/IdeaProjects/stream-api-learn/src/main/resources/file/test.txt"), Charset.defaultCharset())
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .parallel()
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * generate和iterate
     */
    public void genarateFunc(){
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);

        //generate的参数是supplier
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

    }
    /**
     * collect
     */
    public void collectFunc(){
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
        //和上面的reduce差不多
//        int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j));

        String shortMenu = menu.stream().map(Dish::getName).collect(joining());

    }
    /**
     * groupBy
     */
    public void groupByFunc(){
        Map<String, List<Dish>> collect = menu.stream().collect(groupingBy(i -> i.getName()));
    }

    /**
     * 分区操作
     */
    public void partitioningFunc(){
        Map<Boolean, List<Dish>> collect = menu.stream().collect(partitioningBy(Dish::isVegetarian));
    }
}
