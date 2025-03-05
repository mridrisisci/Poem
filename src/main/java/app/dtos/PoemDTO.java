package app.dtos;

import app.entities.Poem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoemDTO
{
    private Integer id;
    private String title;
    private String content;
    private String author;
    private Poem poem;

    public PoemDTO(Poem poem)
    {
        this.id = poem.getId();
        this.title = poem.getTitle();
        this.content = poem.getContent();
        this.author = poem.getAuthor();
    }

}
