package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.StorageData;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@Slf4j
public abstract class AbstractService<T extends StorageData> {
    private Long counter = 0L;
    Storage<T> storage;

    public T create(T data) {
        validate(data);
        data.setId(++counter);
        storage.create(data);
        log.info("Присвоен Id = {}", data.getId());
        return data;
    }

    protected abstract void validate(T data);

    public T get(long id) {
        return storage.get(id);
    }

    public T update(T data) {
        storage.get(data.getId());
        data.setId(data.getId());
        validate(data);
        storage.update(data);
        return data;
    }

    public List<T> getAll() {
        return storage.getAll();
    }

}
