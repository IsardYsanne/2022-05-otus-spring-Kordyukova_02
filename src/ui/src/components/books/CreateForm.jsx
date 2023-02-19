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
    const [selectedImage, setSelectedImage] = useState("");
    const [base64URL, setBase64URL] = useState("");

    const {
        show,
        setShow,
        isBookForm,
        isAuthorForm,
        setIsBookForm,
        setIsAuthorForm
    } = props;

    props.image(selectedImage);

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

    const addBook = () => {
        fetch(addBookUrl, {
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                title,
                authors,
                genre,
                base64URL
            })
        }).then(() => setIsCreated(!isCreated));
    };

    const addAuthor = () => {
        fetch(addAuthorUrl, {
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                name
            })
        }).then(() => setIsCreated(!isCreated));
    };

    const addGenre = () => {
        fetch(addGenreUrl, {
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                genreName
            })
        }).then(() => setIsCreated(!isCreated));
    };

    const getBase64 = (file) => {
        return new Promise(resolve => {
            let baseURL = "";
            let reader = new FileReader();

            reader.readAsDataURL(file);

            reader.onload = () => {
                baseURL = reader.result;
                console.log(baseURL);
                resolve(baseURL);
            };
        });
    };

    const handleFileInputChange = e => {

       let selectedImage = e.target.files[0];

       getBase64(selectedImage)
            .then(result => {
                selectedImage["base64"] = result;
                console.log("File ", selectedImage);
                let res = result.replace('data:image/jpeg;base64,', '');
                setBase64URL(res);
                setSelectedImage(selectedImage);
            })
            .catch(err => {
                console.log(err);
            });
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
                    id={3}
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
                <label htmlFor={4} className={library.formLabel}>
                    Загрузите обложку книги:
                </label>
                <input
                    type={"file"}
                    className={library.inputImage}
                    onChange={(event) => {
                        handleFileInputChange(event);
                    }}
                />
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