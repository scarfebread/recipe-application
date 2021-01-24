import React, {useEffect, useState, createRef} from 'react';
import ApiClient from "../utility/ApiClient";

const CreateRecipe = (props) => {
    const [recipeName, setRecipeName] = useState('');
    const [createdRecipe, setCreatedRecipe] = useState();
    const [showModal, setShowModal] = useState(false);
    const [error, setError] = useState();
    const modal = createRef();
    let submitted = false;

    useEffect(() => {
        const handleClickOffModal = (event) => {
            if (showModal && event.target === modal.current) {
                setShowModal(false)
            }
        }

        window.onclick = function(event) {
            handleClickOffModal(event);
        };

        return () => {
            window.removeEventListener('onclick', handleClickOffModal)
        }
    });

    useEffect(() => {
        const handleEscapeKey = (event) => {
            if (event.key === 'Escape' && showModal) {
                setShowModal(false)
            }
        }

        window.onkeydown = function(event) {
            handleEscapeKey(event);
        };

        return () => {
            window.removeEventListener('onkeydown', handleEscapeKey)
        }
    });

    const createRecipe = () => {
        if (submitted) {
            return false;
        }

        submitted = true;

        setError(null);

        if (recipeName.length === 0) {
            setError('Invalid recipe name!');
            return false;
        }

        const success = (recipe) => {
            setCreatedRecipe(recipe);
            submitted = false;
            props.addRecipe(recipe);
        }

        const failure = (error) => {
            setError(error);
            submitted = false;
        }

        ApiClient.post('/api/recipe', {title: recipeName}, success, failure);
    }

    const renderModal = () => (
        <div ref={modal} className="modal">
            <div className="modal-content">
                <span className="close modalClose" onClick={() => setShowModal(false)}>&times;</span>
                {!createdRecipe &&
                    <div>
                        <label htmlFor="recipeName">Enter the recipe title</label><br/>
                        <input
                            className="modalInput"
                            value={recipeName}
                            onChange={event => setRecipeName(event.target.value)}
                            onKeyUp={(event) => {
                                if (event.code === 'Enter') {
                                    createRecipe();
                                }
                            }}
                        />
                        {error && <label className="error">{error}</label>}
                        <button
                            className="button"
                            onClick={createRecipe}
                            style={{marginTop: 5}}
                        >
                            <span>CREATE</span>
                        </button>
                    </div>
                }
                {createdRecipe &&
                    <div>
                        <label>Recipe successfully created!</label><br/>
                        <label>Click <a href={'/recipe?id=' + createdRecipe.id}>here</a> to view the recipe</label>
                    </div>
                }
            </div>
        </div>
    );

    return (
        <>
            <button className="button" onClick={() => setShowModal(true)}>
                CREATE NEW RECIPE
            </button>
            {showModal && renderModal()}
        </>
    );
}

export default CreateRecipe;
