package app.dtos;

import app.entities.Poem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoemDTO
{
    private String title;
    private String content;
    private String author;


    public PoemDTO(Poem poem)
    {
        this.title = poem.getTitle();
        this.content = poem.getContent();
        this.author = poem.getAuthor();
    }

}
