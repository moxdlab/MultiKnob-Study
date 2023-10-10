package opencv

interface KeypointParam {
    var area: BlobParam
    var circularity: BlobParam
    var inertia: BlobParam
    var convexity: BlobParam
}

object FingertipBlobParams : KeypointParam {
    override var area: BlobParam = BlobParam.NoFilter // BlobParam(200f, Float.MAX_VALUE)
    override var circularity: BlobParam = BlobParam.NoFilter // BlobParam(0f, 1f)
    override var inertia: BlobParam = BlobParam.NoFilter //BlobParam(0f, 1f)
    override var convexity: BlobParam = BlobParam.NoFilter // BlobParam(0f, 1f)
}

object RotationBlobParams : KeypointParam {
    override var area: BlobParam = BlobParam.Filter(200f, 1500f) //TODO: area was the main factor
    override var circularity: BlobParam = BlobParam.Default
    override var inertia: BlobParam = BlobParam.Default
    override var convexity: BlobParam = BlobParam.Default
}

sealed class BlobParam {
    object Default: BlobParam()
    object NoFilter: BlobParam()
    class Filter(val min: Float, val max: Float): BlobParam()
}

sealed class BlurParam {
    abstract var ksize: Double

    object General : BlurParam() {
        override var ksize: Double = 20.0
    }

    object Rotation : BlurParam() {
        override var ksize: Double = 4.0
    }

    object CapCircle : BlurParam() {
        override var ksize: Double = 6.0
    }
}

object ThresParam {
    var lowestWhite: Double = 250.0
    var dilateKernelsize: Int? = 4
    var erodeKernelsize: Int? = null //8
    var dilateFirst: Boolean = true
}

object MaskParam {
    var innerPlus = 18
    var outerPlus = 225
    var rotationCapPlus = 10
}