    import java.io.FileInputStream;
    import java.net.URI;
    import java.net.http.HttpClient;
    import java.net.http.HttpRequest;
    import java.net.http.HttpResponse;
    import java.net.http.HttpResponse.BodyHandlers;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.util.Properties;

    public class App {

        public static void main(String[] args) throws Exception {

            // Configurar o caminho do projeto e acessar as propriedades 
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            String imdbConfigPath = rootPath + "imdb.properties";

            // Configurar os emojis
            // Unicode emoji https://unicode.org/emoji/charts/full-emoji-list.html#1f600
            // Conversor de código 
            String starEmoji = "\u2B50";
            // Usar emoji para as notas 
            // 9 a 10
            // String grinningFaceEmoji = "\uD83D\uDE00";
            // 7 a 8
            // String smileFaceEmoji = "\uD83D\uDE42";
            //5 a 6
            // String neutralFaceEmoji = "\uD83D\uDE10";
            // 3 a 5
            // String disapointedFaceEmoji = "\uD83D\uDE1E";
            // 1 a 3
            // String sleepyFaceEmoji = "\uD83D\uDE2A";

            Properties imdbProps = new Properties();
            imdbProps.load(new FileInputStream(imdbConfigPath));

            String imdbKey = imdbProps.getProperty("appKey");

            // fazer uma conexão HTTP e buscar os top 250 filmes
            // String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json";
            String baseURL = "https://imdb-api.com/en/API/";
            List <String> endpoints = new ArrayList<>();
            endpoints.add("Top250Movies");
            endpoints.add("Top250TVs");
            endpoints.add("MostPopularMovies");
            endpoints.add("MostPopularTVs");
            
            var parser = new JsonParser();
            
             for (String endpoint : endpoints) {
                String address = baseURL + endpoint + "/" + imdbKey;
                //String address = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json";
                URI topMovieAddress = URI.create(address);
                var client = HttpClient.newHttpClient();
                var request = HttpRequest.newBuilder(topMovieAddress).GET().build();
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                String body = response.body();

                List<Map<String, String>> listaDeFilmes = parser.parse(body);

                // exibir e manipular os dados 
                for (Map<String,String> filme : listaDeFilmes) {
                    System.out.printf("\u001b[1m \u001b[43m Movie Title: %s \u001b[m %n", filme.get("title"));
                    System.out.printf("\u001b[1m \u001b[42m Movie Poster: %s  \u001b[m %n", filme.get("image"));
                    System.out.printf("\u001b[35;1m \u001b[45m imDb Rating: %s \u001b[m %n", filme.get("imDbRating"));
                    
                    float rateInt = Float.parseFloat(filme.get("imDbRating"));

                    for (int rate=0; rate < rateInt; rate++){
                        System.out.printf("\u001b[44m%s \u001b[m", starEmoji);
                    }
                    
                    String experience = "";
                    
                    if (rateInt >= 8){
                        experience = "\uD83D\uDE00";
                    }else if (rateInt < 8 && rateInt >= 6 ){
                        experience = "\uD83D\uDE42";   
                    }else if (rateInt < 6 && rateInt >= 5 ){
                        experience = "\uD83D\uDE10";
                    }else if (rateInt < 5 && rateInt >= 3 ){
                        experience = "\uD83D\uDE1E";
                    }else {
                        experience = "\uD83D\uDE2A";
                    }


                    System.out.printf("%n \u001b[47m User experience: %s \u001b[m %n ", experience);

                    System.out.println("\n");
            }
            
        }
            

        }
    }
