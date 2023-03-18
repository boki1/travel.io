import img from '../../assets/rome_pic.webp'
import img2 from "../../assets/dubai.webp"
import './styles.css'

export default function Destinations() {
    return (
        <div className="container" id="destinations">
                <div className="wrapper">
                    <div className="header header_home">Rome</div>
                    <div className="content content_home">You can enjoy good whether and amazing food</div>
                    <div className="image" style={{backgroundImage: `url(${img})`}}></div>
                </div>
                <div className="wrapper">
                    <div className="header header_home">Rome</div>
                    <div className="content content_home">You can spend every cent you have on attractionssddf sdf dsf sd sdf sdfds fsd sdf sdf sdf sd ds fxvc xv cxv xv cxvdfgd fdfsdf sdf sdf dsf sdf sf fdg dfg
                        fdg fdg dggfd hjbjbhj  hj klkm lkm lk n nm nm mkjnn kjnkj nkj nkj n gggggggg  j kj </div>
                    <div className="image" style={{backgroundImage: `url(${img2})`}}></div>
                </div>
        </div>

    );
}