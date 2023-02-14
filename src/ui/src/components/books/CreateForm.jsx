import React, {useEffect, useState} from 'react';
import library from "../../styles/library.module.scss";

const addBookUrl = 'http://localhost:8080/books/save';
const addAuthorUrl = 'http://localhost:8080/authors/save';
const addGenreUrl = 'http://localhost:8080/genres/save';

const getAllAuthors = 'http://localhost:8080/authors/show_all';
const getAllGenres = 'http://localhost:8080/genres/show_all';

const CreateForm = (props) => {

    const [authorsArray, setAuthorsArray] = useState([]);
    const [genreArray, setGenreArray] = useState([]);

    const [name, setName] = useState("");
    const [genreName, setGenreName] = useState("");

    const [authors, setAuthors] = useState(name);
    const [genre, setGenre] = useState(genreName);
    const [title, setTitle] = useState("");
    const [isCreated, setIsCreated] = useState(false);

    const {
        show,
        setShow,
        isBookForm,
        isAuthorForm,
        setIsBookForm,
        setIsAuthorForm
    } = props;

    const validate = (data) => {
        return !data;
    }

    const showForm = () => {
        setShow(!show);
    };

    const rejectBookForm = () => {
        setShow(!show);
        setIsBookForm(!isBookForm);
    };

    const rejectAuthorForm = () => {
        setShow(!show);
        setIsAuthorForm(!isAuthorForm);
    };

    useEffect(() => {
        fetch(getAllAuthors)
            .then(response => response.json())
            .then(auth => setAuthorsArray(auth));
    }, []);

    useEffect(() => {
        fetch(getAllGenres)
            .then(response => response.json())
            .then(genres => setGenreArray(genres));
    }, []);

    const newObjectId = () =>  {
        const timestamp = Math.floor(new Date().getTime() / 1000).toString(16);
        const objectId = timestamp + 'xxxxxxxxxxxxxxxx'.replace(/[x]/g, () => {
            return Math.floor(Math.random() * 16).toString(16);
        }).toLowerCase();

        return objectId;
    }

    const addBook = () => {
        let id  = newObjectId();

        fetch(addBookUrl, {
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                id,
                title,
                authors,
                genre
            })
        }).then(() => setIsCreated(!isCreated));
    };

    const addAuthor = () => {
        let id  = newObjectId();

        fetch(addAuthorUrl, {
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                id,
                name
            })
        }).then(() => setIsCreated(!isCreated));
    };

    const addGenre = () => {
        let id  = newObjectId();

        fetch(addGenreUrl, {
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                id,
                genreName
            })
        }).then(() => setIsCreated(!isCreated));
    };

    return (
        isBookForm ?
        <div className={library.addBookForm}>
            <div className={library.formTitle}>Создать книгу</div>
            <form className={library.formWrapper}>
                <label htmlFor={1} className={library.formLabel}>
                    Введите название книги
                </label>
                <input
                    id={1}
                    className={library.inputForm}
                    type={"text"}
                    onChange={(e) => {
                        e.preventDefault();
                        setTitle(e.target.value);
                    }}
                    pattern={"\\s*\\S+.*"}
                    required
                />
                <label htmlFor={2} className={library.formLabel}>
                    Выберите автора книги
                </label>
                <select
                    id={2}
                    className={library.inputForm}
                    value={authors}
                    onChange={(e) => {
                        setAuthors(e.target.value);
                    }}
                    required
                >
                    {authorsArray && authorsArray.map((author) => (
                        <option
                            key={author.id}
                            value={author.name}
                        >
                            {author.name}
                        </option>
                        )
                    )}
                </select>
                <label htmlFor={3} className={library.formLabel}>
                    Выберите жанр книги
                </label>
                <select
                    id={2}
                    className={library.inputForm}
                    value={genre}
                    onChange={(e) => {
                        setGenre(e.target.value);
                    }}
                    required
                >
                    {genreArray && genreArray.map((genre) => (
                        <option
                            key={genre.id}
                            value={genre.genreName}
                        >
                            {genre.genreName}
                        </option>
                        )
                    )}
                </select>
                <div className={library.btn_container}>
                    <button
                        className={library.btnSubmit}
                        type={"submit"}
                        onClick={addBook}
                        disabled={validate(title) || validate(authorsArray) || validate(genreArray)}
                    >
                        Сохранить
                    </button>
                    <button
                        className={library.btnExit}
                        onClick={rejectBookForm}
                    >
                        Отмена
                    </button>
                </div>
            </form>
        </div>
        : isAuthorForm ?
            <div className={library.addBookForm}>
                <div className={library.formTitle}>Создать автора</div>
                <form className={library.formWrapper}>
                    <label htmlFor={1} className={library.formLabel}>
                        Введите имя автора
                    </label>
                    <input
                        id={1}
                        className={library.inputForm}
                        type={"text"}
                        onChange={(e) => {
                            e.preventDefault();
                            setName(e.target.value);
                        }}
                        pattern={"\\s*\\S+.*"}
                        required
                    />
                    <div className={library.btn_container}>
                        <button
                            className={library.btnSubmit}
                            type={"submit"}
                            onClick={addAuthor}
                            disabled={validate(name)}
                        >
                            Сохранить
                        </button>
                        <button
                            className={library.btnExit}
                            onClick={rejectAuthorForm}
                        >
                            Отмена
                        </button>
                    </div>
                </form>
            </div>
            :
            <div className={library.addBookForm}>
                <div className={library.formTitle}>Создать жанр</div>
                <form className={library.formWrapper}>
                    <label htmlFor={1} className={library.formLabel}>
                        Введите название жанра
                    </label>
                    <input
                        id={1}
                        className={library.inputForm}
                        type={"text"}
                        onChange={(e) => {
                            e.preventDefault();
                            setGenreName(e.target.value);
                        }}
                        pattern={"\\s*\\S+.*"}
                        required
                    />
                    <div className={library.btn_container}>
                        <button
                            className={library.btnSubmit}
                            type={"submit"}
                            onClick={addGenre}
                            disabled={validate(genreName)}
                        >
                            Сохранить
                        </button>
                        <button
                            className={library.btnExit}
                            onClick={showForm}
                        >
                            Отмена
                        </button>
                    </div>
                </form>
            </div>
    );
};

export default CreateForm;