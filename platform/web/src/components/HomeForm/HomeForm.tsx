import React from 'react'
import { FloatingLabel, Form, Row, Col, Button } from 'react-bootstrap';

interface Props {
    description: string;
    setDescription: React.Dispatch<React.SetStateAction<string>>;

    onButtonClick: Function;
}

const HomeForm: React.FC<Props> = ({ description, setDescription, onButtonClick }) => {
    return (
        <Form>
            <FloatingLabel
                controlId="floatingTextarea"
                label="Description"
                className="mb-3"
            >
                <Form.Control type="text" placeholder="Enter Description" value={description} onInput={(event: React.FormEvent<HTMLInputElement>) => setDescription(event.currentTarget.value)} />
                <Form.Control type="number" placeholder="Enter Max price" value={description} onInput={(event: React.FormEvent<HTMLInputElement>) => window.alert(event.currentTarget.value)} />
                <Form.Control type="text" placeholder="Eneter city/Country" value={description} onInput={(event: React.FormEvent<HTMLInputElement>) => window.alert(event.currentTarget.value)} />
                <Form.Control type="date" placeholder="Enter date1" value={description} onInput={(event: React.FormEvent<HTMLInputElement>) => window.alert(event.currentTarget.value)} />
                <Form.Control type="date" placeholder="Enter date2" value={description} onInput={(event: React.FormEvent<HTMLInputElement>) => window.alert(event.currentTarget.value)} />
                <Button variant="primary" type="submit" onClick={(event: React.MouseEvent) => onButtonClick(event)}>
                    Submit
                </Button>
            </FloatingLabel>
        </Form>



    )
}

export default HomeForm