package com.example.note.mapper;

import com.example.note.entity.Note;
import com.example.note.dto.NoteModel;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {


        public NoteModel toDTO(Note task) {
            if (task == null) {
                return null;
            }

            NoteModel taskModel = new NoteModel();
            taskModel.setId(task.getId());
            taskModel.setTitle(task.getTitle());
            taskModel.setStatus(task.getStatus());

                taskModel.setUserId(task.getUser().getId());

            return taskModel;
        }


    public Note toEntity(NoteModel dto) {
        Note task = new Note();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setStatus(dto.getStatus());
        return task;
    }
}
