/**
 * @file OptionDTO.java
 * @brief Opción de respuesta en la API REST.
 * @author Santiago Caicedo
 * @author Ivan Alexander Lopez
 * @author Adrian Araujo Urbano
 * @author Carlos Bambague
 */
package co.unicauca.iso2.parcial2.common.dto;

import java.io.Serializable;

public class OptionDTO implements Serializable {
    private String id;   // "A", "B", "C", "D"
    private String text;

    public OptionDTO() {}

    public OptionDTO(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
