import React, {useState} from 'react';
import styles from "../../styles/book.module.scss";
import {library} from "@fortawesome/fontawesome-svg-core";
import {faSquareXmark, faPenToSquare} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const editBookUrl = 'http://localhost:8080/books/update';
const removeBookUrl = 'http://localhost:8080/books/delete';

const Book = (props) => {

    const [status, setStatus] = useState("");
    const [title, setTitle] = useState("");
    const [showForm, setShowForm] = useState(false);

    library.add(faSquareXmark);
    library.add(faPenToSquare);

    const {
        key,
        book
    } = props;

    const showFormFunction = () => {
        setShowForm(!showForm);
    };

    const validate = (data) => {
        return !data;
    }

    const editBook = (title, id) => {
        fetch(editBookUrl + '?title=' + title + '&' + 'id=' + id, {
            method: 'post'
        }).then(() => setStatus('Update successful'));
    };

    const removeBook = (id) => {
        if (window.confirm("Вы действительно хотите удалить эту книгу?")) {
            fetch(removeBookUrl + '?id=' + id, {
                method: 'delete'
            }).then(() => setStatus('Delete successful'));
        }
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
                <div className={styles.btn_container}>
                    <button
                        className={styles.btnSubmit}
                        type={"submit"}
                        onClick={() => editBook(title, book.id)}
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
                    <div>*тут будет обложка книги*</div>
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