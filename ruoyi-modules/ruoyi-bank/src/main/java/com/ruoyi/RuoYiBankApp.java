package com.ruoyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: TODO
 * @Author 卟哭！
 * @Date 2024/03/04  21:27
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.ruoyi.mapper")
public class RuoYiBankApp {
    public static void main(String[] args) {
        SpringApplication.run(RuoYiBankApp.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  仓储中心模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
