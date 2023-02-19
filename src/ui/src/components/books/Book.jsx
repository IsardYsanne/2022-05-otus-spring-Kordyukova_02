import React, {useState} from 'react';
import styles from "../../styles/book.module.scss";
import {library} from "@fortawesome/fontawesome-svg-core";
import {faSquareXmark, faPenToSquare} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const editBookUrl = 'http://localhost:8080/books/update';
const removeBookUrl = 'http://localhost:8080/books/delete';
const editImageBookUrl = 'http://localhost:8080/books/update_image';

const Book = (props) => {

    const [status, setStatus] = useState("");
    const [title, setTitle] = useState("");
    const [showForm, setShowForm] = useState(false);
    const [selectedImage, setSelectedImage] = useState("");
    const [base64URL, setBase64URL] = useState("");

    library.add(faSquareXmark);
    library.add(faPenToSquare);

    const {
        book,
        image
    } = props;

    const showFormFunction = () => {
        setShowForm(!showForm);
    };

    const validate = (data) => {
        return !data;
    }

    const editBook = (title, book) => {
        fetch(editBookUrl, {
            method: 'put',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                id: book.id,
                title,
                authors: book.authors,
                genre: book.genre,
                base64URL,
            })
        });
    };

    const removeBook = (id) => {
        if (window.confirm("Вы действительно хотите удалить эту книгу?")) {
            fetch(removeBookUrl + '?id=' + id, {
                method: 'delete'
            }).then(() => setStatus('Delete successful')).then(() => window.location.reload(false));
        }
    };

    const removeImage = (book) => {
        let isDeleteImage = true;
        if (window.confirm("Вы действительно хотите удалить обложку?")) {
            fetch(editImageBookUrl, {
                method: 'put',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    id: book.id,
                    title: book.title,
                    authors: book.authors,
                    genre: book.genre,
                    base64URL: book.base64URL,
                    isDeleteImage
                })
            }).then(() => window.location.reload(false));
        }
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
                console.log("File is ", selectedImage);
                let res = result.replace('data:image/jpeg;base64,', '');
                setBase64URL(res);
                setSelectedImage(selectedImage);
            })
            .catch(err => {
                console.log(err);
            });
    };

    return (
        showForm ?
            <form className={styles.formWrapper}>
                <label htmlFor={1} className={styles.formLabel}>
                    Введите название книги
                </label>
                <input
                    id={1}
                    className={styles.inputForm}
                    type={"text"}
                    onChange={(e) => {
                        e.preventDefault();
                        setTitle(e.target.value);
                    }}
                    pattern={"\\s*\\S+.*"}
                    placeholder={book.title}
                    required
                />
                <label htmlFor={2} className={styles.formLabel}>
                    Загрузите обложку книги:
                </label>
                <input
                    type={"file"}
                    className={styles.inputImage}
                    onChange={(event) => {
                        handleFileInputChange(event);
                    }}
                />
                <div className={styles.btn_container}>
                    <button
                        className={styles.btnSubmit}
                        type={"submit"}
                        onClick={() => editBook(title, book)}
                        disabled={validate(title)}
                    >
                        Сохранить
                    </button>
                    <button
                        className={styles.btnExit}
                        onClick={showFormFunction}
                    >
                        Отмена
                    </button>
                </div>
            </form>
            :
            <div className={styles.book_wrapper}>
                <div className={styles.book_column}>
                    <div className={styles.book_title}>
                        {book.title}
                    </div>
                    <div>
                        {book.base64URL ?
                            <div className={styles.image_container}>
                                <img alt="обложка не задана" src={`data:image/jpeg;base64, ${book.base64URL}`}/>
                                <FontAwesomeIcon
                                    icon="fa-solid fa-square-xmark"
                                    className={styles.remove_image_icon}
                                    onClick={() => removeImage(book)}
                                />
                            </div>
                            : "Изображение не задано."
                        }
                    </div>
                    <div className={styles.book_authors}>
                        {book.authors}
                    </div>
                    <div className={styles.book_genre}>
                        {book.genre}
                    </div>
                    <div className={styles.book_menu}>
                        <div className={styles.book_edit}>
                            <FontAwesomeIcon
                                icon="fa-solid fa-pen-to-square"
                                className={styles.edit_icon}
                                onClick={showFormFunction}
                            />
                        </div>
                        <div className={styles.book_remove}>
                            <FontAwesomeIcon
                                icon="fa-solid fa-square-xmark"
                                className={styles.remove_icon}
                                onClick={() => removeBook(book.id)}
                            />
                        </div>
                    </div>
                </div>
            </div>
    );
};

export default Book;