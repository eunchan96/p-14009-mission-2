package com.back;

import java.util.*;

public class App {
    Scanner scanner = new Scanner(System.in);

    public void run(){
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String cmd = scanner.nextLine().trim();
            Rq rq = new Rq(cmd);

            switch (rq.getActionName()) {
                case "종료" -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                case "등록" -> actionWrite();
                case "목록" -> actionList();
                case "삭제" -> actionDelete(rq);
                case "수정" -> actionModify(rq);
                case "빌드" -> actionArchive();
                default -> System.out.println("알 수 없는 명령어입니다.");
            }
        }
    }

    private void actionWrite() {
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        int lastId = Integer.parseInt(Util.file.get("db/wiseSaying/lastId.txt", "0"));
        WiseSaying wiseSaying = new WiseSaying(++lastId, content, author);
        fileWrite(wiseSaying);

        System.out.println("%d번 명언이 등록되었습니다.".formatted(lastId));
    }

    private void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<WiseSaying> wiseSayings = findAll();

        for (WiseSaying wiseSaying : wiseSayings.reversed()) {
            System.out.println(wiseSaying);
        }
    }

    private void actionDelete(Rq rq) {
        int id = rq.getParamInt("id", -1);

        if (id == -1) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }

        String filePath = "db/wiseSaying/%d.json".formatted(id);
        if (Util.file.notExists(filePath)) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        Util.file.delete(filePath);
        System.out.println("%d번 명언이 삭제되었습니다.".formatted(id));
    }

    private void actionModify(Rq rq) {
        int id = rq.getParamInt("id", -1);

        if (id == -1) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }

        String filePath = "db/wiseSaying/%d.json".formatted(id);
        if (Util.file.notExists(filePath)) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        WiseSaying wiseSaying = findById(id);
        System.out.println("명언(기존) : %s".formatted(wiseSaying.getContent()));
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();
        System.out.println("작가(기존) : %s".formatted(wiseSaying.getAuthor()));
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        fileModify(wiseSaying, content, author, filePath);

        System.out.println("%d번 명언이 수정되었습니다.".formatted(id));
    }

    private void actionArchive() {
        String json = Util.json.toString(findAll().stream()
                .map(WiseSaying::toMap)
                .toList());

        Util.file.set("db/wiseSaying/data.json", json);
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

    private void fileWrite(WiseSaying wiseSaying) {
        String filePath = "db/wiseSaying/%d.json".formatted(wiseSaying.getId());
        String wiseSayingJson = Util.json.toString(wiseSaying.toMap());
        Util.file.set(filePath, wiseSayingJson);
        Util.file.set("db/wiseSaying/lastId.txt", String.valueOf(wiseSaying.getId()));
    }

    private WiseSaying findById(int id) {
        String filePath = "db/wiseSaying/%d.json".formatted(id);
        String wiseSayingJson = Util.file.get(filePath, "");
        Map<String, Object> wiseSayingMap = Util.json.toMap(wiseSayingJson);
        return new WiseSaying(wiseSayingMap);
    }

    private List<WiseSaying> findAll() {
        return Util.file.walkRegularFiles("db/wiseSaying", "\\d+\\.json")
                .map(path -> Util.file.get(path.toString(), ""))
                .map(Util.json::toMap)
                .map(WiseSaying::new)
                .toList();
    }

    private void modify(WiseSaying wiseSaying, String content, String author) {
        wiseSaying.setContent(content);
        wiseSaying.setAuthor(author);
    }

    private void fileModify(WiseSaying wiseSaying, String content, String author, String filePath) {
        modify(wiseSaying, content, author);

        String wiseSayingJson = Util.json.toString(wiseSaying.toMap());
        Util.file.set(filePath, wiseSayingJson);
    }
}