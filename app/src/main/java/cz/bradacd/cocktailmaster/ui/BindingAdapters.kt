package cz.bradacd.cocktailmaster.ui

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter


// TODO na zobrazení obrázku je potřeba context, takže to udělat podobně jako je v Mars photos
// TODO - nějak vymyslet jak šikovně říct odkud se bude vlastně ten obrázek brát
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {

}