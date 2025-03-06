package app.entities;

import app.dtos.PoemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "poem")
public class Poem
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String style;
    private String title;
    private String content;
    private String author;

    public Poem(String title, String content, String author)
    {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Poem(String title)
    {
        this.title = title;
    }

    public Poem(PoemDTO poemDTO)
    {
        this.title = poemDTO.getTitle();
        this.author = poemDTO.getAuthor();
        this.content = poemDTO.getContent();
    }



}
